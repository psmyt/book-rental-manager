package org.example.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
    @NotNull
    String serial;
}
