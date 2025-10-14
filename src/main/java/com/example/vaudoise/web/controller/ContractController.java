package com.example.vaudoise.web.controller;

import com.example.vaudoise.web.dto.ContractCreateRequest;
import com.example.vaudoise.web.dto.ContractResponse;
import com.example.vaudoise.app.service.ContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
}
