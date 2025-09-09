package com.domino.smerp.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsBySsn(String ssn);

    Optional<User>findByLoginId(String username);

    Optional<User> findByEmpNo(String empNo);

    @Query(value = "SELECT emp_no FROM user WHERE emp_no LIKE :yearMonth% ORDER BY emp_no DESC LIMIT 1", nativeQuery = true)
    String findLastEmpNoByYearMonth(@Param("yearMonth") String yearMonth);
}
