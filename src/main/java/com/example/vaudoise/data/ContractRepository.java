package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;
import org.springframework.data.domain.Sort;



public interface ContractRepository extends JpaRepository<Contract, UUID> {
    
    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        AND c.deletedAt IS NULL
    """)
    List<Contract> findByClientId(@Param("clientId") UUID clientId);
    
    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        AND c.startDate > :date
        AND c.deletedAt IS NULL
    """)
    List<Contract> findByClientIdAndStartDateAfter(@Param("clientId") UUID clientId, @Param("date") LocalDate date);
    
    
    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        AND c.deletedAt IS NULL
        AND c.startDate <= CURRENT_DATE
        AND (c.endDate IS NULL OR c.endDate >= CURRENT_DATE)
    """)
    List<Contract> findActiveContractsByClientId(@Param("clientId") UUID clientId, Sort sort);

    

    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        AND c.deletedAt IS NULL
    """)
    List<Contract> findAllByClientId(@Param("clientId") UUID clientId, Sort sort);

    @Query("""
        SELECT COALESCE(SUM(c.amount), 0)
        FROM Contract c
        WHERE c.client.id = :clientId
        AND c.deletedAt IS NULL
        AND c.startDate <= :currentDate
        AND (c.endDate IS NULL OR c.endDate >= :currentDate)
    """)
    BigDecimal sumActiveContractsAmount(@Param("clientId") UUID clientId,
                                        @Param("currentDate") LocalDate currentDate);

}
