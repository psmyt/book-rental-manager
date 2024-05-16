package org.example.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue
    UUID id;
    @Column(nullable = false)
    String phone;
    @Column(nullable = false)
    String firstName;
    @Column(nullable = false)
    String lastName;
    String patronymic;
}
