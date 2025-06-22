package com.example.networkperf.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.networkperf.backend.model.TestRun;

public interface TestRunRepository extends JpaRepository<TestRun, Long> {
    
}
