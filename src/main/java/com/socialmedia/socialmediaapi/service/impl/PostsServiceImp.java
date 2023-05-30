package com.socialmedia.socialmediaapi.service.impl;

import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import com.socialmedia.socialmediaapi.repository.PostsRepository;
import com.socialmedia.socialmediaapi.service.PostsService;
import com.socialmedia.socialmediaapi.utils.StringUtil;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
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


    public PostsServiceImp(PostsRepository postsRepo) {
        this.postsRepo = postsRepo;
    }

    @Override
    public List<Posts> getAllByUser(Optional<User> userOptional) {
        return userOptional.map(postsRepo::getAllByUserOwner).orElse(null);
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
        StringUtil.ValidationId(id);
        Optional<Posts> postsOptional = postsRepo.findById(Integer.parseInt(id));
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
}
