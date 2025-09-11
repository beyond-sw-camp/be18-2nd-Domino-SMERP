package com.domino.smerp.client;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByCompanyName(String companyName);
    boolean existsByCompanyName(String companyName);
    boolean existsByBusinessNumber(String businessNumber);
}
