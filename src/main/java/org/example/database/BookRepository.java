package org.example.database;

import org.example.entity.Book;

import java.util.List;

public interface BookRepository extends Repository<Book> {
    List<Book> allBooks();
}
