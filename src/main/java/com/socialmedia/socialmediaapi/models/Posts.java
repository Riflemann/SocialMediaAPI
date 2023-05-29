package com.socialmedia.socialmediaapi.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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

}
