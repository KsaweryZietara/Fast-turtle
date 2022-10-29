package com.example.fastturtle.Controllers;

import com.example.fastturtle.Dtos.CreateCommentDto;
import com.example.fastturtle.Dtos.ReturnCommentDto;
import com.example.fastturtle.Exceptions.CommentNotFoundException;
import com.example.fastturtle.Exceptions.PostNotFoundException;
import com.example.fastturtle.Exceptions.UserNotFoundException;
import com.example.fastturtle.Models.Comment;
import com.example.fastturtle.Models.User;
import com.example.fastturtle.Repositories.CommentRepository;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/comment/")
public class CommentController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final Mapper mapper;

    @Autowired
    public CommentController(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, Mapper mapper) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    @GetMapping("{id}")
    public ReturnCommentDto getCommentById(@PathVariable Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));

        return mapper.toReturnCommentDto(comment);
    }

    @GetMapping("post/{id}/{page}")
    public List<ReturnCommentDto> getCommentsUnderPost(@PathVariable Long id, @PathVariable int page){
        if(!postRepository.existsById(id)){
            throw new PostNotFoundException(id);
        }

        Pageable pageable = PageRequest.of(page, 7, Sort.by("creationTime"));

        List<Comment> comments = commentRepository.findAllByPostId(id, pageable);

        return mapper.toReturnCommentDto(comments);
    }

    @PostMapping
    public ResponseEntity<ReturnCommentDto> createComment(@Valid @RequestBody CreateCommentDto createCommentDto, UriComponentsBuilder builder){
        User user = userRepository.findById(createCommentDto.userId())
                .orElseThrow(() -> new UserNotFoundException(createCommentDto.userId()));

        if(!postRepository.existsById(createCommentDto.postId())){
            throw new PostNotFoundException(createCommentDto.postId());
        }

        Comment comment = mapper.toComment(createCommentDto);

        Comment createdComment = commentRepository.save(comment);
        createdComment.setUser(user);

        URI location = builder.replacePath("api/v1/comment/{id}").buildAndExpand(createdComment.getId()).toUri();
        return ResponseEntity.created(location).body(mapper.toReturnCommentDto(comment));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id){
        if (!commentRepository.existsById(id)){
            throw new CommentNotFoundException(id);
        }

        commentRepository.deleteById(id);
    }
}
