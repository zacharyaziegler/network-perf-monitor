package com.example.networkperf.backend;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.networkperf.backend.model.TestRun;
import com.example.networkperf.backend.repository.TestRunRepository;

@DataJpaTest
@Testcontainers
public class TestRunRepositoryTests {
    
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14")
        .withDatabaseName("test")
        .withUsername("postgres")
        .withPassword("postgres");

    @DynamicPropertySource
    static void pgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRunRepository repository;

    @Test
    void saveAndFind() {
        TestRun run = new TestRun();
        run.setTimestamp(Instant.now());
        run.setLatencyMs(12.3);
        run.setPacketLossPct(0.5);
        run.setDownloadMbps(100.0);
        run.setUploadMbps(20.0);

        repository.save(run);

        List<TestRun> all = repository.findAll();
        assertThat(all).hasSize(1);
        TestRun fetched = all.get(0);
        assertThat(fetched.getLatencyMs()).isEqualTo(12.3);
        assertThat(fetched.getDownloadMbps()).isEqualTo(100.0);
    }
}
