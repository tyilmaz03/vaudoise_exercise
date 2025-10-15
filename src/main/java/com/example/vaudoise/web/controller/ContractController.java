package com.example.vaudoise.web.controller;

import com.example.vaudoise.web.dto.ContractCreateRequest;
import com.example.vaudoise.web.dto.ContractResponse;
import com.example.vaudoise.app.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping
    public ResponseEntity<ContractResponse> createContract(@RequestBody ContractCreateRequest request) {
        ContractResponse created = contractService.createContract(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/client/{clientId}/active")
    public List<ContractResponse> getActiveContractsByClient(@PathVariable UUID clientId) {
        return contractService.getActiveContractsByClient(clientId);
    }

    @GetMapping("/client/{clientId}")
    public List<ContractResponse> getAllContractsByClient(@PathVariable UUID clientId) {
        return contractService.getAllContractsByClient(clientId);
    }

    @GetMapping("/client/{clientId}/active/total")
    public ResponseEntity<Map<String, Object>> getActiveContractsTotal(@PathVariable UUID clientId) {
        BigDecimal total = contractService.getActiveContractsTotal(clientId);
        Map<String, Object> response = Map.of(
                "clientId", clientId,
                "totalActiveAmount", total,
                "currency", "CHF"
        );
        return ResponseEntity.ok(response);
    }
    
}
