package com.example.networkperf.backend.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_runs")
public class TestRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private double latencyMs;

    @Column(nullable = false)
    private double packetLossPct;

    @Column(nullable = false)
    private double downloadMbps;

    @Column(nullable = false)
    private double uploadMbps;

    public Long getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public double getLatencyMs() {
        return latencyMs;
    }

    public void setLatencyMs(double latencyMs) {
        this.latencyMs = latencyMs;
    }

    public double getPacketLossPct() {
        return packetLossPct;
    }

    public void setPacketLossPct(double packetLossPct) {
        this.packetLossPct = packetLossPct;
    }

    public double getDownloadMbps() {
        return downloadMbps;
    }

    public void setDownloadMbps(double downloadMbps) {
        this.downloadMbps = downloadMbps;
    }

    public double getUploadMbps() {
        return uploadMbps;
    }

    public void setUploadMbps(double uploadMbps) {
        this.uploadMbps = uploadMbps;
    }
}
