package com.example.fastturtle.Controllers;

import com.example.fastturtle.Dtos.CreatePostDto;
import com.example.fastturtle.Dtos.ReturnPostDto;
import com.example.fastturtle.Exceptions.PostNotFoundException;
import com.example.fastturtle.Exceptions.UserNotFoundException;
import com.example.fastturtle.Models.Post;
import com.example.fastturtle.Models.User;
import com.example.fastturtle.Repositories.PostRepository;
import com.example.fastturtle.Repositories.UserRepository;
import com.example.fastturtle.Services.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/post/")
public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Mapper mapper;

    @Autowired
    public PostController(PostRepository postRepository, UserRepository userRepository, Mapper mapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ReturnPostDto getPostById(@PathVariable Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return Mapper.toReturnPostDto(post);
    }

    @GetMapping("profile/{id}/{page}")
    public List<ReturnPostDto> getPostsByUserId(@PathVariable Long id, @PathVariable int page){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        Pageable pageable = PageRequest.of(page, 2, Sort.by("creationTime"));

        List<Post> posts = postRepository.findAllByUserId(id, pageable);

        return Mapper.toReturnPostDto(posts);
    }

    @PostMapping
    public ResponseEntity<ReturnPostDto> createPost(@RequestBody CreatePostDto createPostDto, UriComponentsBuilder builder){
        if(!userRepository.existsById(createPostDto.userId())){
            throw new UserNotFoundException(createPostDto.userId());
        }

        Post post = Mapper.toPost(createPostDto);
        Post createdPost = postRepository.save(post);

        URI location = builder.replacePath("api/v1/post/{id}").buildAndExpand(createdPost.getId()).toUri();
        return ResponseEntity.created(location).body(Mapper.toReturnPostDto(createdPost));
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
