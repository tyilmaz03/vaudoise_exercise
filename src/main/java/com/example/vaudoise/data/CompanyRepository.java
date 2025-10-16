package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Company;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("""
        SELECT c FROM Company c
        WHERE c.companyIdentifier = :companyIdentifier
        AND c.deletedAt IS NULL
    """)
    Optional<Company> findByCompanyIdentifier(@Param("companyIdentifier") String companyIdentifier);

}