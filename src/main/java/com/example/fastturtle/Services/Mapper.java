package com.example.fastturtle.Services;

import com.example.fastturtle.Dtos.*;
import com.example.fastturtle.Models.Comment;
import com.example.fastturtle.Models.Post;
import com.example.fastturtle.Models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Mapper {
    public ReturnUserDto toReturnUserDto(User user){
        return new ReturnUserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    public List<ReturnUserDto> toReturnUserDto(List<User> users){
        ArrayList<ReturnUserDto> result = new ArrayList<>();

        for(User u : users){
            result.add(toReturnUserDto(u));
        }

        return result;
    }

    public User toUser(CreateUserDto createUserDto){
        return new User(createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.email(),
                createUserDto.password());
    }

    public ReturnPostDto toReturnPostDto(Post post){
        return new ReturnPostDto(post.getId(),
                post.getUser().getFirstName(),
                post.getUser().getLastName(),
                post.getCreationTime(),
                post.getContent());
    }

    public List<ReturnPostDto> toReturnPostDto(List<Post> posts){
        ArrayList<ReturnPostDto> result = new ArrayList<>();

        for(Post p : posts){
            result.add(toReturnPostDto(p));
        }

        return result;
    }

    public Post toPost(CreatePostDto createPostDto){
        return new Post(new User(createPostDto.userId()),
                createPostDto.content());
    }

    public ReturnCommentDto toReturnCommentDto(Comment comment){
        return new ReturnCommentDto(comment.getId(),
                comment.getUser().getFirstName(),
                comment.getUser().getLastName(),
                comment.getCreationTime(),
                comment.getContent());
    }

    public List<ReturnCommentDto> toReturnCommentDto(List<Comment> comments){
        ArrayList<ReturnCommentDto> result = new ArrayList<>();

        for(Comment c : comments){
            result.add(toReturnCommentDto(c));
        }

        return result;
    }

    public Comment toComment(CreateCommentDto createCommentDto){
        return new Comment(new User(createCommentDto.userId()),
                new Post(createCommentDto.postId()),
                createCommentDto.content());
    }
}
