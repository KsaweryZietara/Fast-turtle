package com.example.fastturtle.Dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record CreatePostDto(
        @NotNull
        @Min(0)
        Long userId,
        @NotBlank
        @Size(min = 1, max = 200)
        String content
){}
