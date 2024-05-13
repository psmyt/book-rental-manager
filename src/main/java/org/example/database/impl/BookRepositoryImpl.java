package org.example.database.impl;

import org.example.database.BookRepository;
import org.example.entity.Book;

import java.util.List;

public class BookRepositoryImpl extends BaseRepository<Book> implements BookRepository {

    @Override
    public List<Book> allBooks() {
        return tryWithNewSession(session ->
                session.createQuery("from Book", Book.class)
                        .getResultList()
        );
    }
}
