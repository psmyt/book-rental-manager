package org.example.database.impl.repository;

import org.example.entity.Book;

import java.util.List;

public class BookRepository extends BaseRepository<Book> {

    public List<Book> allBooks() {
        return tryWithNewSession(session ->
                session.createQuery("from Book", Book.class)
                        .getResultList()
        );
    }
}
