package com.example.fastturtle.Repositories;

import com.example.fastturtle.Models.Post;
import com.example.fastturtle.Models.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    public List<Post> findAllByUserId(Long id, Pageable pageable);
}
