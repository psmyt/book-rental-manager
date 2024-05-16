package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.database.BookRepository;
import org.example.service.BookService;
import org.example.dto.BookDto;
import org.example.entity.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public List<BookDto> getBooks() {
        return bookRepository.findAll()
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
