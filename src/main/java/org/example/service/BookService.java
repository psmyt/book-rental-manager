package org.example.service;

import org.example.dto.BookDto;

import java.util.List;
import java.util.UUID;

public interface BookService {
    List<BookDto> getBooks();
    BookDto getBook(UUID id);
}
