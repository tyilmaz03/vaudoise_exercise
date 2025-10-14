package com.example.vaudoise.app.mapper;

import com.example.vaudoise.core.model.*;
import com.example.vaudoise.web.dto.ClientResponse;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public ClientResponse toResponse(Client client) {
        ClientResponse dto = new ClientResponse();
        dto.setId(client.getId());
        dto.setName(client.getName());
        dto.setEmail(client.getEmail());
        dto.setPhone(client.getPhone());

        if (client instanceof Person person) {
            dto.setType("PERSON");
            dto.setBirthdate(person.getBirthdate().toString());
        } else if (client instanceof Company company) {
            dto.setType("COMPANY");
            dto.setCompanyIdentifier(company.getCompanyIdentifier());
        }

        return dto;
    }
}
