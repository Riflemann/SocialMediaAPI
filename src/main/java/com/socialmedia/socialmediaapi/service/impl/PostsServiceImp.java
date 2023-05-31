package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.PostsRepository;
import com.socialmedia.socialmediaapi.repository.UserRepository;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.management.AttributeNotFoundException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PostsServiceImp implements PostsService {

    @Value("${path.to.file.folder}")
    private String filePath;

    private final PostsRepository postsRepo;

    private final UserRepository userRepo;


    public PostsServiceImp(PostsRepository postsRepo, UserRepository userRepo) {
        this.postsRepo = postsRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<Posts> getAllByUser(Optional<User> userOptional) {
        return userOptional.map(postsRepo::getAllByUserOwner).orElse(null);
    }
    @Override
    public List<Posts> getAllByUserId(String id) throws IncorrectIdException {
        return postsRepo.getAllByUserOwnerId(StringUtil.ValidationId(id));
    }

    @Override
    public Page<Posts> getAllFromFriends(String id, int offset, int limit) throws IncorrectIdException {
        int intId = StringUtil.ValidationId(id);
        Page<Posts> postsPage = postsRepo.findAll(PageRequest.of(offset, limit, Sort.by("creatingTime")));
        postsPage.stream().filter(posts -> posts.getId() != intId).close();
        return postsPage;

//        List<Posts> postsList = new ArrayList<>();
//        Optional<User> user = userRepo.findById(intId);
//        if (user.isPresent()) {
//            List<Friends> friendsList = user.get().getFriendsList();
//            for (Friends friend : friendsList) {
//                int userToId = friend.getUserTo().getId();
//                postsList.addAll(postsRepo.getAllByUserOwnerId(userToId));
//            }
//            return postsList;
//        } else {
//            throw new UserNotFoundException("Пользователь с ID " + id + " не найден");
//        }
    }

    @Override
    public void savePost(Posts posts) {

        postsRepo.save(posts);
    }

    @Override
    public void editPost(String header, String text, String pic, String id) {
        postsRepo.editPost(
                header,
                text,
                pic,
                String.valueOf(id));
    }

    @Override
    public String getPic(String id) throws AttributeNotFoundException, IncorrectIdException {
        Optional<Posts> postsOptional = postsRepo.findById(StringUtil.ValidationId(id));
        if (postsOptional.isPresent()) {
            return postsOptional.get().getPic();
        } else {
            System.out.println("Изображение не найдено");
            throw new AttributeNotFoundException();
        }
    }

    @Override
    public String saveImage(MultipartFile pic) throws IOException {
        File storageFile = new File(filePath + "/" + pic.getName());
        Path path = Path.of(filePath, pic.getName());
        try (FileOutputStream fos = new FileOutputStream(storageFile)) {
            Files.createFile(path);
            IOUtils.copy(pic.getInputStream(), fos);
        }
        return path.toString();
    }

    @Override
    public boolean deletePost(int postId){
        postsRepo.deleteById(postId);
        return true;
    }
}
