package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue
    UUID id;
    @Column(nullable = false)
    String isbn;
}
