package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Company;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    Optional<Company> findByCompanyIdentifier(String companyIdentifier);
}