package com.example.vaudoise.web.controller;

import com.example.vaudoise.web.dto.ClientCreateRequest;
import com.example.vaudoise.web.dto.ClientResponse;
import com.example.vaudoise.app.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
