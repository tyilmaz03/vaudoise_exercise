package com.example.vaudoise.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ContractCreateRequest {

    private UUID clientId;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
}
