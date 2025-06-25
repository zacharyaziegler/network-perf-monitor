package com.example.networkperf.backend.service;

import java.io.IOException;


public interface PingExecutor {
    /**
     * Runs one ping and returns the full stdout as a single String
     */
    String execPing() throws IOException, InterruptedException;
}
