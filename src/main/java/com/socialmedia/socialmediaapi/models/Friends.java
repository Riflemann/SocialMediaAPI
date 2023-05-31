package com.socialmedia.socialmediaapi.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.Objects;

@Entity
public class Friends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "user_from")
    private User userFrom;

    @ManyToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "user_to")
    private User userTo;

    private int status;

    public Friends(int id, User userFrom, User userTo, int status) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.status = status;
    }

    public Friends() {
    }

    public static FriendsBuilder builder() {
        return new FriendsBuilder();
    }

    public int getId() {
        return this.id;
    }

    public User getUserFrom() {
        return this.userFrom;
    }

    public User getUserTo() {
        return this.userTo;
    }

    public int getStatus() {
        return this.status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friends friends = (Friends) o;
        return id == friends.id && status == friends.status && Objects.equals(userFrom, friends.userFrom) && Objects.equals(userTo, friends.userTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userFrom, userTo, status);
    }

    public String toString() {
        return "Friends(id=" + this.getId() + ", userFrom=" + this.getUserFrom() + ", userTo=" + this.getUserTo() + ", status=" + this.getStatus() + ")";
    }

    public static class FriendsBuilder {
        private int id;
        private User userFrom;
        private User userTo;
        private int status;

        FriendsBuilder() {
        }

        public FriendsBuilder id(int id) {
            this.id = id;
            return this;
        }

        public FriendsBuilder userFrom(User userFrom) {
            this.userFrom = userFrom;
            return this;
        }

        public FriendsBuilder userTo(User userTo) {
            this.userTo = userTo;
            return this;
        }

        public FriendsBuilder status(int status) {
            this.status = status;
            return this;
        }

        public Friends build() {
            return new Friends(id, userFrom, userTo, status);
        }

        public String toString() {
            return "Friends.FriendsBuilder(id=" + this.id + ", userFrom=" + this.userFrom + ", userTo=" + this.userTo + ", status=" + this.status + ")";
        }
    }
}
