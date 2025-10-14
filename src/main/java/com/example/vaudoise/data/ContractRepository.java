package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findByClientId(UUID clientId);
}
