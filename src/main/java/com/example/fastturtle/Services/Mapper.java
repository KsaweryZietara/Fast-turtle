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
    public static ReturnCommentDto toReturnCommentDto(Comment comment){
        return new ReturnCommentDto(comment.getId(),
                comment.getUser().getFirstName(),
                comment.getUser().getLastName(),
                comment.getCreationTime(),
                comment.getContent());
    }

    public static List<ReturnCommentDto> toReturnCommentDto(List<Comment> comments){
        ArrayList<ReturnCommentDto> result = new ArrayList<>();

        for(Comment c : comments){
            result.add(toReturnCommentDto(c));
        }

        return result;
    }

    public static ReturnUserDto toReturnUserDto(User user){
        return new ReturnUserDto(user.getId(), user.getFirstName(), user.getLastName());
    }

    public static List<ReturnUserDto> toReturnUserDto(List<User> users){
        ArrayList<ReturnUserDto> result = new ArrayList<>();

        for(User u : users){
            result.add(toReturnUserDto(u));
        }

        return result;
    }

    public static User toUser(CreateUserDto createUserDto){
        return new User(createUserDto.firstName(),
                createUserDto.lastName(),
                createUserDto.email(),
                createUserDto.password());
    }

    public static ReturnPostDto toReturnPostDto(Post post){
        return new ReturnPostDto(post.getId(),
                post.getUser().getFirstName(),
                post.getUser().getLastName(),
                post.getCreationTime(),
                post.getContent());
    }

    public static List<ReturnPostDto> toReturnPostDto(List<Post> posts){
        ArrayList<ReturnPostDto> result = new ArrayList<>();

        for(Post p : posts){
            result.add(toReturnPostDto(p));
        }

        return result;
    }

    public static Post toPost(CreatePostDto createPostDto){
        return new Post(new User(createPostDto.userId()),
                createPostDto.content());
    }

}
