package com.example.fastturtle.Models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "post")
    List<Comment> comments;

    public Post() {
    }

    public Post(Long id) {
        this.id = id;
    }

    public Post(User user, String content) {
        this.user = user;
        this.creationTime = Timestamp.valueOf(LocalDateTime.now());
        this.content = content;
    }

    public Post(Long id, User user, Timestamp creationTime, String content, List<Comment> comments) {
        this.id = id;
        this.user = user;
        this.creationTime = creationTime;
        this.content = content;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
