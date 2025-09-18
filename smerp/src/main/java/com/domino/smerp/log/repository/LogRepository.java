package com.domino.smerp.log.repository;

import com.domino.smerp.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
