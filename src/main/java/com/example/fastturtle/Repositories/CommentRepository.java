package com.example.fastturtle.Repositories;

import com.example.fastturtle.Models.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    public List<Comment> findAllByPostId(Long id, Pageable pageable);
}
