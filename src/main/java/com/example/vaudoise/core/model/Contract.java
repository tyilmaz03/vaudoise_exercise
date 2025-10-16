package com.example.vaudoise.core.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter(AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contracts")
public class Contract {

    @Id
    @GeneratedValue
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false, updatable = false)
    private Client client;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDate updateDate;

    @Column
    private LocalDate deletedAt;

    public Contract(Client client, BigDecimal amount, LocalDate startDate, LocalDate endDate) {
        this.client = client;
        this.amount = amount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.updateDate = LocalDate.now();
    }

    public void close(LocalDate endDate) {
        this.endDate = endDate;
        this.updateDate = LocalDate.now();
    }

    public void updateAmount(BigDecimal amount) {
        this.amount = amount;
        this.updateDate = LocalDate.now();
    }

    public void updateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null) this.startDate = startDate;
        if (endDate != null) this.endDate = endDate;
        this.updateDate = LocalDate.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDate.now();
    }

}
