package com.example.vaudoise.app.mapper;

import com.example.vaudoise.core.model.Contract;
import com.example.vaudoise.web.dto.ContractResponse;
import org.springframework.stereotype.Component;

@Component
public class ContractMapper {

    public ContractResponse toResponse(Contract contract) {
        return new ContractResponse(
                contract.getId(),
                contract.getClient().getId(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getAmount()
        );
    }
}
