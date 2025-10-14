package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
