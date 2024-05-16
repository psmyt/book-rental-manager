package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class BookCopy {
    @Id
    @GeneratedValue
    UUID id;
    @ManyToOne
    Book book;
    @Column(nullable = false)
    String serial;
}
