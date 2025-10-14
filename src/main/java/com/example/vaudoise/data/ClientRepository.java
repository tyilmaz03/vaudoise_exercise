package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    Optional<Client> findByEmail(String email);
    Optional<Client> findByPhone(String phone);
}
