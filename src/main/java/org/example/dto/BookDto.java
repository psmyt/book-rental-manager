package org.example.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BookDto {
    private final UUID id;
    private final String isbn;
}
