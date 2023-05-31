package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.FriendsRepository;
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
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostsServiceImp implements PostsService {

    @Value("${path.to.file.folder}")
    private String filePath;

    private final PostsRepository postsRepo;

    private final FriendsRepository friendsRepo;


    public PostsServiceImp(PostsRepository postsRepo, FriendsRepository friendsRepo) {
        this.postsRepo = postsRepo;
        this.friendsRepo = friendsRepo;
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
    public Page<Posts> getAllPostsFromFriends(String id, int offset, int limit) throws IncorrectIdException {

        int intId = StringUtil.ValidationId(id);

        List<Friends> friendsArrayList = new ArrayList<>();
        friendsArrayList.addAll(friendsRepo.getAllFriends(intId));
        friendsArrayList.addAll(friendsRepo.getAllSubscribes(intId));

        List<Integer> friendsIdArrayList;
        friendsIdArrayList = friendsArrayList.stream()
                                                    .map(friends -> friends.getUserTo().getId())
                                                    .collect(Collectors.toCollection(ArrayList::new));

        List<Integer> finalFriendsIdArrayList = friendsIdArrayList;

        Page<Posts> postsPage = postsRepo.findAll(
                                                PageRequest.of(
                                                            offset,
                                                            limit,
                                                            Sort.by("creatingTime")));
        postsPage.stream()
                        .filter(posts -> finalFriendsIdArrayList.contains(posts.getUserOwner().getId()))
                        .close();
        return postsPage;
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
