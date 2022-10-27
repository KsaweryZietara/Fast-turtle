package com.example.fastturtle.Dtos;

import java.sql.Timestamp;

public record ReturnCommentDto(Long id, String firstName, String lastName, Timestamp creationTime, String content) {
}
