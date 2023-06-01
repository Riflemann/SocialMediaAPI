package com.socialmedia.socialmediaapi.dto;

import com.socialmedia.socialmediaapi.models.Friends;
import com.socialmedia.socialmediaapi.models.Posts;
import com.socialmedia.socialmediaapi.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPage {
    private String fullName;
    private String city;
    private List<Posts> posts;
    private List<Friends> friends;

    public <E> UserPage(int userId, String john_doe, String s, String s1, ArrayList<E> es) {

    }
}
