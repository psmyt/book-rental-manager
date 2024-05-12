package org.example.database.impl.repository;

import org.example.entity.BookRental;
import org.example.entity.RentalStatus;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class RentalRepository extends BaseRepository<BookRental> {
    private static final int ENTRIES_PER_PAGE = 10;
    private static final String HISTORY = "from BookRental " +
            "where " +
            "client.id = :clientId " +
            "and status not in ( 'RESERVATION_EXPIRED' ) " +
            "order by startDate desc";
    private static final String FIND_BY_BOOK_CLIENT_STATUS = "from BookRental " +
            "where " +
            "client.id = :clientId and " +
            "status in ( :statuses ) and " +
            "bookCopy.book.id = :bookId";

    private static final String DEACTIVATE_EXPIRED = "update book_rental " +
            "set status = 'RESERVATION_EXPIRED' " +
            "where status = 'RESERVED' " +
            "and created + 10 * interval '1 minute' < now()";

    public List<BookRental> findByBookIdAndClientIdAndStatusIn(UUID bookId, UUID clientId, RentalStatus... statuses) {
        return tryWithNewSession(session ->
                session.createQuery(FIND_BY_BOOK_CLIENT_STATUS, BookRental.class)
                        .setParameter("clientId", clientId)
                        .setParameter("statuses", Arrays.asList(statuses))
                        .setParameter("bookId", bookId)
                        .getResultList()
        );
    }

    public void deactivateExpired() {
        tryWithNewSession(session ->
                session.createNativeQuery(DEACTIVATE_EXPIRED).executeUpdate()
        );
    }

    public Stream<BookRental> history(UUID clientId, int page) {
        int offset = page * ENTRIES_PER_PAGE;
        return tryWithNewSession(session ->
                session.createQuery(HISTORY, BookRental.class)
                        .setParameter("clientId", clientId)
                        .setFirstResult(offset)
                        .setMaxResults(ENTRIES_PER_PAGE)
                        .getResultStream()
        );
    }
}
