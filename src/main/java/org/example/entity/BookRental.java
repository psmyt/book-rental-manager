package org.example.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookRental {
    @Id
    @GeneratedValue
    UUID id;
    @ManyToOne
    BookCopy bookCopy;
    @ManyToOne
    Client client;
    @CreationTimestamp
    LocalDateTime created;
    @UpdateTimestamp
    LocalDateTime lastModified;
    LocalDate startDate;
    LocalDate endDate;
    @Enumerated(EnumType.STRING)
    RentalStatus status;
}

