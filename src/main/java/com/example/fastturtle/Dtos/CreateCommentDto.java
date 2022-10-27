package com.example.fastturtle.Dtos;

public record CreateCommentDto(Long userId, Long postId, String content) {
}
