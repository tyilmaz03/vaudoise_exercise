package com.example.vaudoise.data;

import com.example.vaudoise.core.model.Client;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c WHERE c.email = :email AND c.deletedAt IS NULL")
    boolean existsByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Client c WHERE c.phone = :phone AND c.deletedAt IS NULL")
    boolean existsByPhone(@Param("phone") String phone);

    @Query("SELECT c FROM Client c WHERE c.email = :email AND c.deletedAt IS NULL")
    Optional<Client> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Client c WHERE c.phone = :phone AND c.deletedAt IS NULL")
    Optional<Client> findByPhone(@Param("phone") String phone);

    @Query("SELECT c FROM Client c WHERE c.deletedAt IS NULL")
    List<Client> findAll();

    @Query("SELECT c FROM Client c WHERE c.id = :id AND c.deletedAt IS NULL")
    Optional<Client> findById(@Param("id") UUID id);

}
