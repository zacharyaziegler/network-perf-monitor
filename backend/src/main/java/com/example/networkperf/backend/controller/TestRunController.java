package com.example.networkperf.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.networkperf.backend.model.TestRun;
import com.example.networkperf.backend.repository.TestRunRepository;

@RestController
@RequestMapping("/api/tests")
public class TestRunController {

    private final TestRunRepository repository;

    public TestRunController(TestRunRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TestRun> getAllRuns() {
        return repository.findAll();
    }
    
}
