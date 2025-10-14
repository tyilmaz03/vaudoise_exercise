package com.example.vaudoise.web.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class ClientResponse {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String type;
    private String birthdate;
    private String companyIdentifier;
}
