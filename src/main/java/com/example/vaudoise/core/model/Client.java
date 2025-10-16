package com.example.vaudoise.core.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
import java.time.LocalDate;
import org.hibernate.annotations.GenericGenerator;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "client_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Client implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid-v7")
    @GenericGenerator(
        name = "uuid-v7",
        type = UuidV7Generator.class
    )
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column
    private LocalDate deletedAt;

    protected Client(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void updateContactInfo(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public void softDelete() {
        this.deletedAt = LocalDate.now();
    }

}
