package com.example.networkperf.backend.service;

import java.io.IOException;
import java.util.stream.Collectors;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProcessPingExecutor implements PingExecutor {
    private final String targetHost;

    public ProcessPingExecutor(@Value("${app.ping.host:8.8.8.8}") String targetHost) {
        this.targetHost = targetHost;
    }

    @Override
    public String execPing() throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("ping", "-c", "1", targetHost);
        Process p = pb.start();
        String output = new BufferedReader(new InputStreamReader(p.getInputStream()))
            .lines().collect(Collectors.joining("\n"));
        p.waitFor();
        return output;
    }
    
}
