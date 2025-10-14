package com.example.vaudoise.core.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "person")
@DiscriminatorValue("PERSON")
public class Person extends Client {

    @Column(name = "birthdate", nullable = false, updatable = false)
    private LocalDate birthdate;

    public Person(String name, String email, String phone, LocalDate birthdate) {
        super(name, email, phone);
        this.birthdate = Objects.requireNonNull(birthdate, "birthdate is required for person");
    }
}