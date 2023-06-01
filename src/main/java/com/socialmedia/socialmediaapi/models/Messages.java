package com.socialmedia.socialmediaapi.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Table(name = "messages")
public class Messages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_one")
    private int userFrom;

    @Column(name = "user_two")
    private int userTo;

    @Column(name = "msg_ldt")
    private LocalDateTime localDateTimeCreated;

    @Column(name = "txt")
    private String txt;

    @Column(name = "is_read")
    private boolean isRead;

    public Messages(int id, int userFrom, int userTo, LocalDateTime localDateTimeCreated, String txt, boolean isRead) {
        this.id = id;
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.localDateTimeCreated = localDateTimeCreated;
        this.txt = txt;
        this.isRead = isRead;
    }

    public Messages() {
    }

    public static MessagesBuilder builder() {
        return new MessagesBuilder();
    }

    public int getId() {
        return this.id;
    }

    public int getUserFrom() {
        return this.userFrom;
    }

    public int getUserTo() {
        return this.userTo;
    }

    public LocalDateTime getLocalDateTimeCreated() {
        return this.localDateTimeCreated;
    }

    public String getTxt() {
        return this.txt;
    }

    public boolean isRead() {
        return this.isRead;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserFrom(int userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(int userTo) {
        this.userTo = userTo;
    }

    public void setLocalDateTimeCreated(LocalDateTime localDateTimeCreated) {
        this.localDateTimeCreated = localDateTimeCreated;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Messages messages = (Messages) o;
        return id == messages.id && userFrom == messages.userFrom && userTo == messages.userTo && isRead == messages.isRead && Objects.equals(localDateTimeCreated, messages.localDateTimeCreated) && Objects.equals(txt, messages.txt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userFrom, userTo, localDateTimeCreated, txt, isRead);
    }

    public String toString() {
        return "Messages(id=" + this.getId() + ", userFrom=" + this.getUserFrom() + ", userTo=" + this.getUserTo() + ", localDateTimeCreated=" + this.getLocalDateTimeCreated() + ", txt=" + this.getTxt() + ", isRead=" + this.isRead() + ")";
    }

    public static class MessagesBuilder {
        private int id;
        private int userFrom;
        private int userTo;
        private LocalDateTime localDateTimeCreated;
        private String txt;
        private boolean isRead;

        MessagesBuilder() {
        }

        public MessagesBuilder id(int id) {
            this.id = id;
            return this;
        }

        public MessagesBuilder userFrom(int userFrom) {
            this.userFrom = userFrom;
            return this;
        }

        public MessagesBuilder userTo(int userTo) {
            this.userTo = userTo;
            return this;
        }

        public MessagesBuilder localDateTimeCreated(LocalDateTime localDateTimeCreated) {
            this.localDateTimeCreated = localDateTimeCreated;
            return this;
        }

        public MessagesBuilder txt(String txt) {
            this.txt = txt;
            return this;
        }

        public MessagesBuilder isRead(boolean isRead) {
            this.isRead = isRead;
            return this;
        }

        public Messages build() {
            return new Messages(id, userFrom, userTo, localDateTimeCreated, txt, isRead);
        }
    }
}
