package com.example.fastturtle.Controllers;

import com.example.fastturtle.Dtos.CreatePostDto;
import com.example.fastturtle.Dtos.ReturnPostDto;
import com.example.fastturtle.Exceptions.PostNotFoundException;
import com.example.fastturtle.Exceptions.UserNotFoundException;
import com.example.fastturtle.Models.Post;
import com.example.fastturtle.Models.User;
import com.example.fastturtle.Repositories.PostRepository;
import com.example.fastturtle.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/post/")
public class PostController {
    private final PostRepository postRepository;

    private final UserRepository userRepository;

    @Autowired
    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("{id}")
    public ReturnPostDto getPostById(@PathVariable Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return new ReturnPostDto(id,
                post.getUser().getFirstName(),
                post.getUser().getLastName(),
                post.getCreationTime(),
                post.getContent());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostDto createPostDto, UriComponentsBuilder builder){
        if(!userRepository.existsById(createPostDto.userId())){
            throw new UserNotFoundException(createPostDto.userId());
        }

        Post post = new Post(new User(createPostDto.userId()),
                Timestamp.valueOf(LocalDateTime.now()),
                createPostDto.content());

        Post createdPost = postRepository.save(post);

        URI location = builder.replacePath("api/v1/post/{id}").buildAndExpand(createdPost.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id){
        if(!postRepository.existsById(id)){
            throw new PostNotFoundException(id);
        }

        postRepository.deleteById(id);
    }
}
