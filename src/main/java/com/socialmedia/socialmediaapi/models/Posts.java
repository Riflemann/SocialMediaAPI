package com.socialmedia.socialmediaapi.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "user_owner")
    private User userOwner;

    @Column(name = "date_time")
    private LocalDateTime creatingTime;

    @Column(name = "header")
    private String header;

    @Column(name = "txt")
    private String text;

    @Column(name = "pic")
    private String pic;

    public Posts(int id, User userOwner, LocalDateTime creatingTime, String header, String text, String pic) {
        this.id = id;
        this.userOwner = userOwner;
        this.creatingTime = creatingTime;
        this.header = header;
        this.text = text;
        this.pic = pic;
    }

    public Posts() {
    }

    public static PostsBuilder builder() {
        return new PostsBuilder();
    }

    public int getId() {
        return this.id;
    }

    public User getUserOwner() {
        return this.userOwner;
    }

    public LocalDateTime getCreatingTime() {
        return this.creatingTime;
    }

    public String getHeader() {
        return this.header;
    }

    public String getText() {
        return this.text;
    }

    public String getPic() {
        return this.pic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserOwner(User userOwner) {
        this.userOwner = userOwner;
    }

    public void setCreatingTime(LocalDateTime creatingTime) {
        this.creatingTime = creatingTime;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Posts posts = (Posts) o;
        return id == posts.id && Objects.equals(userOwner, posts.userOwner) && Objects.equals(creatingTime, posts.creatingTime) && Objects.equals(header, posts.header) && Objects.equals(text, posts.text) && Objects.equals(pic, posts.pic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userOwner, creatingTime, header, text, pic);
    }

    public String toString() {
        return "Posts(id=" + this.getId() + ", userOwner=" + this.getUserOwner() + ", creatingTime=" + this.getCreatingTime() + ", header=" + this.getHeader() + ", text=" + this.getText() + ", pic=" + this.getPic() + ")";
    }

    public static class PostsBuilder {
        private int id;
        private User userOwner;
        private LocalDateTime creatingTime;
        private String header;
        private String text;
        private String pic;

        PostsBuilder() {
        }

        public PostsBuilder id(int id) {
            this.id = id;
            return this;
        }

        public PostsBuilder userOwner(User userOwner) {
            this.userOwner = userOwner;
            return this;
        }

        public PostsBuilder creatingTime(LocalDateTime creatingTime) {
            this.creatingTime = creatingTime;
            return this;
        }

        public PostsBuilder header(String header) {
            this.header = header;
            return this;
        }

        public PostsBuilder text(String text) {
            this.text = text;
            return this;
        }

        public PostsBuilder pic(String pic) {
            this.pic = pic;
            return this;
        }

        public Posts build() {
            return new Posts(id, userOwner, creatingTime, header, text, pic);
        }
    }
}
