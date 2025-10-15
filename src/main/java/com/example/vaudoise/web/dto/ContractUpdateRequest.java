package com.example.vaudoise.web.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ContractUpdateRequest {

    @DecimalMin(value = "0.0", inclusive = true, message = "Amount cannot be negative")
    private BigDecimal amount;

    private LocalDate startDate;

    private LocalDate endDate;
}
