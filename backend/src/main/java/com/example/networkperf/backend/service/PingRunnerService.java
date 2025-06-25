package com.example.networkperf.backend.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.networkperf.backend.model.TestRun;
import com.example.networkperf.backend.repository.TestRunRepository;

@Service
public class PingRunnerService {

    private final TestRunRepository repo;
    private final PingExecutor executor;

    // pull cron and host from application.yml
    public PingRunnerService(TestRunRepository repo, PingExecutor executor) {
        this.repo = repo;
        this.executor = executor;
    }

    @Scheduled(cron = "${app.ping.cron}")
    public void runPing() {
        try {
            String out = executor.execPing();
            double latency = parseAvgLatency(out);
            double lossPct = parsePacketLoss(out);
            TestRun tr = new TestRun(Instant.now(), latency, lossPct, 0.0, 0.0);
            repo.save(tr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double parseAvgLatency(String pingOutput) {
        // typical line: "rtt min/avg/max/mdev = 14.123/15.456/16.789/0.123 ms"
        Pattern p = Pattern.compile("min/avg/max.*=\\s*[^/]*/([^/]+)/");
        Matcher m = p.matcher(pingOutput);
        return m.find() ? Double.parseDouble(m.group(1)) : 0.0;
    }

    public double parsePacketLoss(String pingOutput) {
        // typical line: "1 packets transmitted, 1 received, 0% packet loss"
        Pattern p = Pattern.compile("(\\d+(?:\\.\\d+)?)% packet loss");
        Matcher m = p.matcher(pingOutput);
        return m.find() ? Double.parseDouble(m.group(1)) : 100.0;
    }
}