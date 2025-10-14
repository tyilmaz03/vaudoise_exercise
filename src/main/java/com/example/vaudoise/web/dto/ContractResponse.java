package com.example.vaudoise.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ContractResponse {

    private UUID id;
    private UUID clientId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
}
