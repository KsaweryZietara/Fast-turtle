package com.example.fastturtle.Dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreateCommentDto(
        @NotNull
        @Min(0)
        Long userId,
        @NotNull
        @Min(0)
        Long postId,
        @NotBlank
        @Size(min = 1, max = 150)
        String content
){}
