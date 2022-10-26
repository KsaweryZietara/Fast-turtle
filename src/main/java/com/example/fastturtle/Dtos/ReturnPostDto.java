package com.example.fastturtle.Dtos;

import java.sql.Timestamp;

public record ReturnPostDto(Long id, String fistName, String lastName, Timestamp creationTime, String content) {
}
