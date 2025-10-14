package com.example.vaudoise.web.dto;

import com.example.vaudoise.core.model.ClientType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ClientCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must have 10 digits")
    private String phone;

    @NotNull(message = "Client type is required")
    private ClientType type;

    //PERSON
    @NotNull(message = "Birthdate is required for PERSON")
    private LocalDate birthdate;

    //COMPANY
    @Pattern(regexp = "^[a-zA-Z]{3}-\\d{3}$", message = "Company identifier must match pattern aaa-123")
    private String companyIdentifier;
}
