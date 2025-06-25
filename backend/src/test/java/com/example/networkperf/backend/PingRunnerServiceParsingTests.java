package com.example.networkperf.backend;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.networkperf.backend.service.PingRunnerService;

public class PingRunnerServiceParsingTests {
    PingRunnerService svc = new PingRunnerService(null, null);

    String sample = """
    PING 8.8.8.8 (8.8.8.8): 56 data bytes
    64 bytes from 8.8.8.8: icmp_seq=0 ttl=117 time=11.123 ms

    --- 8.8.8.8 ping statistics ---
    1 packets transmitted, 1 packets received, 0.0% packet loss
    round-trip min/avg/max/stddev = 11.123/11.123/11.123/0.000 ms
    """;

    @Test
    void parseAvgLatency_extractsCorrectly() {
        double avg = svc.parseAvgLatency(sample);
        assertThat(avg).isCloseTo(11.123, within(1e-6));
    }

    @Test
    void parsePacketLoss_extractsCorrectly() {
        double loss = svc.parsePacketLoss(sample);
        assertThat(loss).isZero();
    }
}
