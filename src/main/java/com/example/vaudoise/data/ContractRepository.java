package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.UUID;
import java.util.List;
import java.time.LocalDate;
import java.math.BigDecimal;


public interface ContractRepository extends JpaRepository<Contract, UUID> {
    List<Contract> findByClientId(UUID clientId);
    
    List<Contract> findByClientIdAndStartDateAfter(UUID clientId, LocalDate date);

    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        AND (c.endDate IS NULL OR c.endDate > :currentDate)
        ORDER BY c.updateDate DESC
    """)
    List<Contract> findActiveContractsByClientId(@Param("clientId") UUID clientId, @Param("currentDate") LocalDate currentDate);
    
    @Query("""
        SELECT c FROM Contract c
        WHERE c.client.id = :clientId
        ORDER BY c.updateDate DESC
    """)
    List<Contract> findAllByClientIdOrderByUpdateDateDesc(@Param("clientId") UUID clientId);

    @Query("""
        SELECT COALESCE(SUM(c.amount), 0)
        FROM Contract c
        WHERE c.client.id = :clientId
        AND (c.endDate IS NULL OR c.endDate > :currentDate)
    """)
    BigDecimal sumActiveContractsAmount(@Param("clientId") UUID clientId,
                                    @Param("currentDate") LocalDate currentDate);
}
