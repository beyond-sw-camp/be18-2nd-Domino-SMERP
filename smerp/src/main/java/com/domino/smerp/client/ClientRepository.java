package com.domino.smerp.client;

import com.domino.smerp.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>,
    QuerydslPredicateExecutor<Client> {
    Optional<Client> findByCompanyName(String companyName);
    boolean existsByCompanyName(String companyName);
    boolean existsByBusinessNumber(String businessNumber);
}
