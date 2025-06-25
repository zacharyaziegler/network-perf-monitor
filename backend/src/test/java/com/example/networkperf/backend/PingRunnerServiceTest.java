package com.example.networkperf.backend;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.networkperf.backend.model.TestRun;
import com.example.networkperf.backend.repository.TestRunRepository;
import com.example.networkperf.backend.service.PingExecutor;
import com.example.networkperf.backend.service.PingRunnerService;

@ExtendWith(MockitoExtension.class)
public class PingRunnerServiceTest {
    @Mock TestRunRepository repo;
    @Mock PingExecutor executor;
    @InjectMocks PingRunnerService svc;

    String fakeOutput =
    "round-trip min/avg/max/stddev = 5.000/7.500/10.000/0.000 ms\n" +
    "1 packets transmitted, 1 received, 20.0% packet loss\n";

    @Test
    void runPing_savesParsedTestRun() throws Exception {
        when(executor.execPing()).thenReturn(fakeOutput);
        
        svc.runPing();

        // capture the TestRun that was saved
        ArgumentCaptor<TestRun> cap = ArgumentCaptor.forClass(TestRun.class);
        verify(repo).save(cap.capture());

        TestRun tr = cap.getValue();
        assertThat(tr.getLatencyMs()).isEqualTo(7.5);
        assertThat(tr.getPacketLossPct()).isEqualTo(20.0);
        assertThat(tr.getTimestamp()).isNotNull();
    }
}
