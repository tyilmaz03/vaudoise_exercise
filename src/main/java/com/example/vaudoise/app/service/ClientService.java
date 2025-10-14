package com.example.vaudoise.app.service;

import com.example.vaudoise.app.mapper.ClientMapper;
import com.example.vaudoise.core.exception.BadRequestException;
import com.example.vaudoise.core.exception.ConflictException;
import com.example.vaudoise.core.model.*;
import com.example.vaudoise.data.ClientRepository;
import com.example.vaudoise.data.CompanyRepository;
import com.example.vaudoise.web.dto.ClientCreateRequest;
import com.example.vaudoise.web.dto.ClientResponse;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.example.vaudoise.core.model.ClientType;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.UUID;
import java.util.NoSuchElementException;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final CompanyRepository companyRepository;

    private final ClientMapper mapper;

    public ClientService(ClientRepository repository,CompanyRepository companyRepository, ClientMapper mapper) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    public ClientResponse createClient(ClientCreateRequest req) {
        List<String> errors = new ArrayList<>();

        if (repository.existsByEmail(req.getEmail())) {
            errors.add("Email already in use");
        }

        if (repository.existsByPhone(req.getPhone())) {
            errors.add("Phone number already in use");
        }

        if (req.getType() == null) {
            errors.add("Client type is required");
        }

        //PERSON
        if (req.getType() == ClientType.PERSON) {
            if (req.getBirthdate() == null) {
                errors.add("Birthdate is required for PERSON");
            } else if (!req.getBirthdate().isBefore(java.time.LocalDate.now())) {
                errors.add("Birthdate must be a past date");
            }
        }

        //COMPANY
        else if (req.getType() == ClientType.COMPANY) {
            if (req.getCompanyIdentifier() == null || req.getCompanyIdentifier().isBlank()) {
                errors.add("Company identifier is required for COMPANY");
            }
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        Client client = (req.getType() == ClientType.PERSON)
                ? new Person(req.getName(), req.getEmail(), req.getPhone(), req.getBirthdate())
                : new Company(req.getName(), req.getEmail(), req.getPhone(), req.getCompanyIdentifier());

        Client saved = repository.save(client);
        return mapper.toResponse(saved);
    }


    public List<ClientResponse> getAllClients() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClientResponse getClientById(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));
    }


    public ClientResponse getClientBy(String email, String phone, String companyIdentifier) {

        if ((email == null || email.isBlank())
                && (phone == null || phone.isBlank())
                && (companyIdentifier == null || companyIdentifier.isBlank())) {
            throw new BadRequestException(List.of("At least one search parameter is required"));
        }

    // Recherche par email
        if (email != null && !email.isBlank()) {
            return repository.findByEmail(email)
                    .map(mapper::toResponse)
                    .orElseThrow(() -> new NoSuchElementException("Client not found with email: " + email));
        }

    // Recherche par téléphone
        if (phone != null && !phone.isBlank()) {
            return repository.findByPhone(phone)
                    .map(mapper::toResponse)
                    .orElseThrow(() -> new NoSuchElementException("Client not found with phone: " + phone));
        }

    // Recherche par identifiant d’entreprise
        if (companyIdentifier != null && !companyIdentifier.isBlank()) {
            return companyRepository.findByCompanyIdentifier(companyIdentifier)
                .map(mapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Client not found with companyIdentifier: " + companyIdentifier));
        }

        throw new BadRequestException(List.of("Invalid search request"));
    }

}
