package org.example.service.impl;

import org.example.database.BookRepository;
import org.example.dto.BookDto;
import org.example.entity.Book;
import org.example.service.BookService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public List<BookDto> getBooks() {
        return bookRepository.allBooks()
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
