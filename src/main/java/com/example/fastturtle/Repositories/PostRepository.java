package com.example.fastturtle.Repositories;

import com.example.fastturtle.Models.Post;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

}
