package com.socialmedia.socialmediaapi.models;

import com.socialmedia.socialmediaapi.security.token.Token;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    private String fullName;
    private String login;
    private String password;
    private String city;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "userFrom",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Messages> messagesList;

    @OneToMany(mappedBy = "userOwner",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Posts> postsList;

    @OneToMany(mappedBy = "userTo",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Friends> friendsList;

    public User(int id, String fullName, String login, String password, String city, Role role, List<Token> tokens, List<Messages> messagesList, List<Posts> postsList, List<Friends> friendsList) {
        this.id = id;
        this.fullName = fullName;
        this.login = login;
        this.password = password;
        this.city = city;
        this.role = role;
        this.tokens = tokens;
        this.messagesList = messagesList;
        this.postsList = postsList;
        this.friendsList = friendsList;
    }

    public User() {
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public int getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getLogin() {
        return this.login;
    }

    public String getPassword() {
        return this.password;
    }

    public String getCity() {
        return this.city;
    }

    public Role getRole() {
        return this.role;
    }

    public List<Token> getTokens() {
        return this.tokens;
    }

    public List<Messages> getMessagesList() {
        return this.messagesList;
    }

    public List<Posts> getPostsList() {
        return this.postsList;
    }

    public List<Friends> getFriendsList() {
        return this.friendsList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void setMessagesList(List<Messages> messagesList) {
        this.messagesList = messagesList;
    }

    public void setPostsList(List<Posts> postsList) {
        this.postsList = postsList;
    }

    public void setFriendsList(List<Friends> friendsList) {
        this.friendsList = friendsList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(fullName, user.fullName) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && Objects.equals(city, user.city) && role == user.role && Objects.equals(tokens, user.tokens) && Objects.equals(messagesList, user.messagesList) && Objects.equals(postsList, user.postsList) && Objects.equals(friendsList, user.friendsList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, login, password, city, role, tokens, messagesList, postsList, friendsList);
    }

    public String toString() {
        return "User(id=" + this.getId() + ", fullName=" + this.getFullName() + ", login=" + this.getLogin() + ", password=" + this.getPassword() + ", city=" + this.getCity() + ", role=" + this.getRole() + ", tokens=" + this.getTokens() + ", messagesList=" + this.getMessagesList() + ", postsList=" + this.getPostsList() + ", friendsList=" + this.getFriendsList() + ")";
    }

    public static class UserBuilder {
        private int id;
        private String fullName;
        private String login;
        private String password;
        private String city;
        private Role role;
        private List<Token> tokens;
        private List<Messages> messagesList;
        private List<Posts> postsList;
        private List<Friends> friendsList;

        UserBuilder() {
        }

        public UserBuilder id(int id) {
            this.id = id;
            return this;
        }

        public UserBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder city(String city) {
            this.city = city;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder tokens(List<Token> tokens) {
            this.tokens = tokens;
            return this;
        }

        public UserBuilder messagesList(List<Messages> messagesList) {
            this.messagesList = messagesList;
            return this;
        }

        public UserBuilder postsList(List<Posts> postsList) {
            this.postsList = postsList;
            return this;
        }

        public UserBuilder friendsList(List<Friends> friendsList) {
            this.friendsList = friendsList;
            return this;
        }

        public User build() {
            return new User(id, fullName, login, password, city, role, tokens, messagesList, postsList, friendsList);
        }
    }
}
