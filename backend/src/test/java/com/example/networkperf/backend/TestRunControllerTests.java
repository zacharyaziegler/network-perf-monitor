package com.example.networkperf.backend;

import com.example.networkperf.backend.model.TestRun;
import com.example.networkperf.backend.repository.TestRunRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TestRunControllerTests {
    
    @Container
    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>("postgres:14")
        .withDatabaseName("test")
        .withUsername("postgres")
        .withPassword("postgres");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", pg::getJdbcUrl);
        registry.add("spring.datasource.username", pg::getUsername);
        registry.add("spring.datasource.password", pg::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestRunRepository repo;

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        TestRun run = new TestRun();
        run.setTimestamp(Instant.parse("2025-06-22T11:00:00Z"));
        run.setLatencyMs(10.0);
        run.setPacketLossPct(0.0);
        run.setDownloadMbps(50.0);
        run.setUploadMbps(10.0);
        repo.save(run);
    }

    @Test
    void getAllRuns_returnsJsonArray() throws Exception {
        mockMvc.perform(get("/api/tests")
            .with(httpBasic("admin", "admin"))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].latencyMs", is(10.0)))
            .andExpect(jsonPath("$[0].downloadMbps", is(50.0)));
    }
        
    @Test
    void getAllRuns_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/tests"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllRuns_withBadAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/tests")
            .with(httpBasic("bad", "creds")))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllRuns_emptyDb_returnsEmptyArray() throws Exception {
        repo.deleteAll();
        mockMvc.perform(get("/api/tests")
            .with(httpBasic("admin", "admin")))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void healthEndpoint_isUpAndUnauthenticated() throws Exception {
        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is("UP")));
    }

}
