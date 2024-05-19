package org.example.rest;

import lombok.AllArgsConstructor;
import org.example.dto.BookDto;
import org.example.dto.RentalView;
import org.example.exception.AlreadyReservedException;
import org.example.service.BookService;
import org.example.exception.NoBookCopiesAvailableException;
import org.example.service.RentalService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/public")
@AllArgsConstructor
public class PublicController {
    private final BookService bookService;
    private final RentalService rentalService;

    @GetMapping("/books")
    public List<BookDto> getBooks() {
        return bookService.getBooks();
    }

    @PostMapping("/books/{bookId}/reserve")
    public RentalView reserve(
            @PathVariable("bookId") UUID bookId,
            @AuthenticationPrincipal(expression = "clientId") UUID clientId
    ) throws AlreadyReservedException, NoBookCopiesAvailableException {
        return rentalService.reserve(bookId, clientId);
    }

    @GetMapping("/rentals/history")
    public List<RentalView> history(@RequestParam("page") int page, @AuthenticationPrincipal(expression = "clientId") UUID clientId) {
        if (page < 0) {
            throw new IllegalArgumentException();
        }
        return rentalService.rentalHistory(clientId, page);
    }
}
