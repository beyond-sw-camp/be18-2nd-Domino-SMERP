package com.domino.smerp.user;

import jakarta.validation.constraints.Pattern;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, QuerydslPredicateExecutor<User> {
    boolean existsByLoginId(String loginId);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsBySsn(String ssn);

    Optional<User>findByLoginId(String username);

    Optional<User> findByEmpNo(String empNo);

    Optional<User> findByName(String name);

    @Query(value = "SELECT emp_no FROM user WHERE emp_no LIKE :yearMonth% ORDER BY emp_no DESC LIMIT 1", nativeQuery = true)
    String findLastEmpNoByYearMonth(@Param("yearMonth") String yearMonth);

    boolean existsByPhoneAndEmpNoNot(
        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식이 올바르지 않습니다.") String phone, String enpNo);
}