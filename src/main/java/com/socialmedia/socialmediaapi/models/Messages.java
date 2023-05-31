package com.socialmedia.socialmediaapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;


@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
