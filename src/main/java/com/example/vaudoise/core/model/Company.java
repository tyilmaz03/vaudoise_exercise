package com.example.vaudoise.core.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@DiscriminatorValue("COMPANY")
public class Company extends Client {


    @Column(name = "company_identifier", nullable = false, updatable = false, length = 7, unique = true)
    private String companyIdentifier;

    public Company(String name, String email, String phone, String companyIdentifier) {
        super(name, email, phone);
        this.companyIdentifier = Objects.requireNonNull(companyIdentifier, "companyIdentifier is required for company");
    }
}
