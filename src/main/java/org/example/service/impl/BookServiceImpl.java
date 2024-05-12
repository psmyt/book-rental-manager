package org.example.service.impl;

import org.example.database.DatabaseAccess;
import org.example.dto.BookDto;
import org.example.entity.Book;
import org.example.service.BookService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private final DatabaseAccess databaseAccess;

    public BookServiceImpl(DatabaseAccess databaseAccess) {
        this.databaseAccess = databaseAccess;
    }


    @Override
    public List<BookDto> getBooks() {
        return databaseAccess.allBooks()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BookDto toDto(Book book) {
        return new BookDto(book.getId(), book.getIsbn());
    }

    @Override
    public BookDto getBook(UUID id) {
        return null;
    }
}
