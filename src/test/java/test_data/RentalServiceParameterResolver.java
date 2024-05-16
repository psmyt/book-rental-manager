package test_data;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.entity.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.UUID;

public class RentalServiceParameterResolver implements ParameterResolver {
    private final Client CLIENT = newClient();
    private final Book BOOK = newBook();
    private final BookCopy BOOK_COPY = newBookCopy(BOOK);
    private final BookRental BOOK_RENTAL = newRental(BOOK_COPY, CLIENT);

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Type type = parameterContext.getParameter().getType();
        return type.equals(Client.class)
                || type.equals(BookRental.class)
                || type.equals(Book.class)
                || type.equals(BookCopy.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Type type = parameterContext.getParameter().getType();

        if (type.equals(Client.class)) {
            return parameterContext.isAnnotated(Random.class) ? newClient() : CLIENT;
        }

        if (type.equals(BookRental.class)) {
            return BOOK_RENTAL;
        }

        if (type.equals(Book.class)) {
            return BOOK;
        }

        if (type.equals(BookCopy.class)) {
            return BOOK_COPY;
        }

        throw new RuntimeException();
    }

    private Book newBook() {
        Book book = new Book();
        book.setId(UUID.randomUUID());
        book.setIsbn(RandomStringUtils.randomNumeric(10));
        return book;
    }

    private BookCopy newBookCopy(Book book) {
        BookCopy bookCopy = new BookCopy();
        bookCopy.setBook(book);
        bookCopy.setId(UUID.randomUUID());
        bookCopy.setSerial(RandomStringUtils.randomNumeric(10));
        return bookCopy;
    }

    private Client newClient() {
        Client client = new Client();
        client.setId(UUID.randomUUID());
        client.setPhone(RandomStringUtils.randomNumeric(10));
        client.setFirstName(RandomStringUtils.randomAlphabetic(5));
        client.setLastName(RandomStringUtils.randomAlphabetic(5));
        return client;
    }

    private BookRental newRental(BookCopy bookCopy, Client client) {
        BookRental bookRental = new BookRental();
        bookRental.setStatus(RentalStatus.RESERVED);
        bookRental.setId(UUID.randomUUID());
        bookRental.setCreated(LocalDateTime.now());
        bookRental.setLastModified(LocalDateTime.now());
        bookRental.setBookCopy(bookCopy);
        bookRental.setClient(client);
        return bookRental;
    }
}
