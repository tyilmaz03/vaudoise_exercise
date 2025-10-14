package com.example.vaudoise.web.controller;

import com.example.vaudoise.web.dto.ClientCreateRequest;
import com.example.vaudoise.web.dto.ClientResponse;
import com.example.vaudoise.app.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@Valid @RequestBody ClientCreateRequest request) {
        ClientResponse created = clientService.createClient(request);
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping
    public List<ClientResponse> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable UUID id) {
        ClientResponse client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/search")
    public ResponseEntity<ClientResponse> searchClient(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String companyIdentifier
    ) {
        ClientResponse client = clientService.getClientBy(email, phone, companyIdentifier);
        return ResponseEntity.ok(client);
    }


}
