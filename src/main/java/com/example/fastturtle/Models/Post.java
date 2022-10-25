package com.example.fastturtle.Models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    Timestamp creationTime;

    String content;
}
