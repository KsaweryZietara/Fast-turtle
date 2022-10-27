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

        return mapper.toReturnPostDto(post);
    }

    @GetMapping("profile/{id}/{page}")
    public List<ReturnPostDto> getPostsByUserId(@PathVariable Long id, @PathVariable int page){
        if(!userRepository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        Pageable pageable = PageRequest.of(page, 2, Sort.by("creationTime").descending());

        List<Post> posts = postRepository.findAllByUserId(id, pageable);

        return mapper.toReturnPostDto(posts);
    }

    @GetMapping("homepage/{id}/{page}")
    public List<ReturnPostDto> getHomePageByUserId(@PathVariable Long id, @PathVariable int page){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        List<Long> ids = new ArrayList<>();
        for (User u : user.getFollowing()){
            ids.add(u.getId());
        }

        Pageable pageable = PageRequest.of(page, 2, Sort.by("creationTime").descending());
        List<Post> posts = postRepository.findAllByUserIdIn(ids, pageable);

        return mapper.toReturnPostDto(posts);
    }

    @PostMapping
    public ResponseEntity<ReturnPostDto> createPost(@RequestBody CreatePostDto createPostDto, UriComponentsBuilder builder){
        User user = userRepository.findById(createPostDto.userId())
                .orElseThrow(() -> new UserNotFoundException(createPostDto.userId()));

        Post post = mapper.toPost(createPostDto);
        Post createdPost = postRepository.save(post);
        createdPost.setUser(user);

        URI location = builder.replacePath("api/v1/post/{id}").buildAndExpand(createdPost.getId()).toUri();
        return ResponseEntity.created(location).body(mapper.toReturnPostDto(createdPost));
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
