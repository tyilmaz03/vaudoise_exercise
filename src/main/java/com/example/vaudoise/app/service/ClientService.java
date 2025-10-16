package com.example.vaudoise.app.service;

import com.example.vaudoise.app.mapper.ClientMapper;
import com.example.vaudoise.core.exception.BadRequestException;
import com.example.vaudoise.core.model.*;
import com.example.vaudoise.data.ClientRepository;
import com.example.vaudoise.data.CompanyRepository;
import com.example.vaudoise.data.ContractRepository;
import com.example.vaudoise.web.dto.ClientCreateRequest;
import com.example.vaudoise.web.dto.ClientResponse;
import com.example.vaudoise.web.dto.ClientUpdateRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository repository;
    private final CompanyRepository companyRepository;
    private final ContractRepository contractRepository;
    private final ClientMapper mapper;

    public ClientService(ClientRepository repository,
                         CompanyRepository companyRepository,
                         ContractRepository contractRepository,
                         ClientMapper mapper) {
        this.repository = repository;
        this.companyRepository = companyRepository;
        this.contractRepository = contractRepository;
        this.mapper = mapper;
    }

    /**
     * Vérifie qu’un client existe et n’est pas supprimé (soft-deleted).
     */
    private Client ensureClientExists(UUID id) {
        Client client = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));

        if (client.getDeletedAt() != null) {
            throw new BadRequestException(List.of("This client has been deleted"));
        }

        return client;
    }

    // --- CREATE ---
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

        if (req.getType() == ClientType.PERSON) {
            if (req.getBirthdate() == null) {
                errors.add("Birthdate is required for PERSON");
            } else if (!req.getBirthdate().isBefore(LocalDate.now())) {
                errors.add("Birthdate must be a past date");
            }
        } else if (req.getType() == ClientType.COMPANY) {
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

    // --- READ (all clients) ---
    public List<ClientResponse> getAllClients() {
        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- READ (by id) ---
    public ClientResponse getClientById(UUID id) {
        Client client = ensureClientExists(id);
        return mapper.toResponse(client);
    }

    // --- READ (by filters) ---
    public ClientResponse getClientBy(String email, String phone, String companyIdentifier) {
        if ((email == null || email.isBlank())
                && (phone == null || phone.isBlank())
                && (companyIdentifier == null || companyIdentifier.isBlank())) {
            throw new BadRequestException(List.of("At least one search parameter is required"));
        }

        Optional<Client> clientOpt = Optional.empty();

        if (email != null && !email.isBlank()) {
            clientOpt = repository.findByEmail(email);
        } else if (phone != null && !phone.isBlank()) {
            clientOpt = repository.findByPhone(phone);
        } else if (companyIdentifier != null && !companyIdentifier.isBlank()) {
            clientOpt = companyRepository.findByCompanyIdentifier(companyIdentifier)
                    .map(c -> (Client) c);
        }

        Client client = clientOpt
                .filter(c -> c.getDeletedAt() == null)
                .orElseThrow(() -> new NoSuchElementException("Client not found or has been deleted"));

        return mapper.toResponse(client);
    }

    // --- UPDATE ---
    public ClientResponse updateClient(UUID id, ClientUpdateRequest req) {
        Client client = ensureClientExists(id);

        if (req == null) {
            throw new BadRequestException(List.of("Update request cannot be null"));
        }

        List<String> errors = new ArrayList<>();

        if (req.getEmail() != null && repository.existsByEmail(req.getEmail()) &&
                !req.getEmail().equals(client.getEmail())) {
            errors.add("Email already in use");
        }

        if (req.getPhone() != null && repository.existsByPhone(req.getPhone()) &&
                !req.getPhone().equals(client.getPhone())) {
            errors.add("Phone number already in use");
        }

        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }

        String name = (req.getName() != null && !req.getName().isBlank()) ? req.getName() : client.getName();
        String email = (req.getEmail() != null && !req.getEmail().isBlank()) ? req.getEmail() : client.getEmail();
        String phone = (req.getPhone() != null && !req.getPhone().isBlank()) ? req.getPhone() : client.getPhone();

        client.updateContactInfo(name, email, phone);
        Client updated = repository.save(client);

        return mapper.toResponse(updated);
    }

    // --- DELETE (soft delete) ---
    @Transactional
    public String deleteClient(UUID id) {
        Client client = ensureClientExists(id);

        LocalDate now = LocalDate.now();

        List<Contract> contracts = contractRepository.findByClientId(id)
                .stream()
                .filter(c -> c.getDeletedAt() == null)
                .toList();

        List<Contract> futureContracts = contracts.stream()
                .filter(c -> c.getStartDate() != null && c.getStartDate().isAfter(now))
                .toList();

        if (!futureContracts.isEmpty()) {
            List<String> details = futureContracts.stream()
                    .map(c -> "contractId=" + c.getId() + ", startDate=" + c.getStartDate())
                    .toList();

            List<String> errors = new ArrayList<>();
            errors.add("Cannot delete client: some contracts start in the future. " +
                    "Delete them first or change startDate to today or a past date.");
            errors.addAll(details);

            throw new BadRequestException(errors);
        }

        client.softDelete();
        repository.save(client);

        for (Contract contract : contracts) {
            if (contract.getEndDate() != null && contract.getEndDate().isBefore(now)) {
                contract.softDelete();
            } else {
                contract.softDeleteWithEndDate();
            }
        }

        contractRepository.saveAll(contracts);
        return "Client soft deleted successfully";
    }
}
