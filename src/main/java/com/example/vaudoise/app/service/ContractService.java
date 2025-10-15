package com.example.vaudoise.app.service;

import com.example.vaudoise.app.mapper.ContractMapper;
import com.example.vaudoise.core.exception.BadRequestException;
import com.example.vaudoise.core.model.Client;
import com.example.vaudoise.core.model.Contract;
import com.example.vaudoise.data.ClientRepository;
import com.example.vaudoise.data.ContractRepository;
import com.example.vaudoise.web.dto.ContractCreateRequest;
import com.example.vaudoise.web.dto.ContractResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ClientRepository clientRepository;
    private final ContractMapper mapper;

    public ContractService(ContractRepository contractRepository, ClientRepository clientRepository, ContractMapper mapper) {
        this.contractRepository = contractRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    @Transactional
    public ContractResponse createContract(ContractCreateRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.getClientId() == null) {
            errors.add("clientId is required");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("amount must be greater than or equal to 0");
        }

        if (request.getEndDate() != null && request.getStartDate() == null) {
            errors.add("startDate must be provided when endDate is provided");
        }

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = request.getEndDate();

        if (endDate != null && startDate.isAfter(endDate)) {
            errors.add("startDate must be before or equal to endDate");
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new BadRequestException(List.of("Client not found with id: " + request.getClientId())));

        Contract contract = new Contract(client, request.getAmount(), startDate, endDate);
        Contract saved = contractRepository.save(contract);

        return mapper.toResponse(saved);
    }

    private void ensureClientExists(UUID clientId) {
        if (!clientRepository.existsById(clientId)) {
            throw new NoSuchElementException("Client not found with id: " + clientId);
        }
    }


    public List<ContractResponse> getActiveContractsByClient(UUID clientId) {
        
        ensureClientExists(clientId);

        List<Contract> contracts = contractRepository.findActiveContractsByClientId(clientId, LocalDate.now());
        
        if (contracts.isEmpty()) {
            throw new NoSuchElementException("No active contract found for client with id: " + clientId);
        }

        return contracts.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<ContractResponse> getAllContractsByClient(UUID clientId) {

        ensureClientExists(clientId);
        List<Contract> contracts = contractRepository.findAllByClientIdOrderByUpdateDateDesc(clientId);
        
        if (contracts.isEmpty()) {
            throw new NoSuchElementException("No contract found for client with id: " + clientId);
        }

        return contracts.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public BigDecimal getActiveContractsTotal(UUID clientId) {
        ensureClientExists(clientId);
        
        BigDecimal total = contractRepository.sumActiveContractsAmount(clientId, LocalDate.now());
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            throw new NoSuchElementException("No active contract found for client with id: " + clientId);
        }

        return total;
    }

}
