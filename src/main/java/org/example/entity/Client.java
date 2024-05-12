package org.example.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue
    UUID id;
    @NotNull
    String phone;
    @NotNull
    String firstName;
    @NotNull
    String lastName;
    String patronymic;
}
