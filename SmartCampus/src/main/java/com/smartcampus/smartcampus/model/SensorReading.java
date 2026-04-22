/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.model;

import java.util.UUID;

/**
 * Model class representing a single sensor reading event.
 * Implements BaseModel for Generic DAO support.
 */
public class SensorReading implements BaseModel {
    private String id; // Unique reading event ID
    private long timestamp; // Epoch time (ms)
    private double value; // Metric value

    public SensorReading() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public SensorReading(double value) {
        this();
        this.value = value;
    }

    @Override
    public String getId() { return id; }

    @Override
    public void setId(String id) { this.id = id; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }
}