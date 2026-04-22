/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.dao;


import com.smartcampus.smartcampus.model.Room;
import com.smartcampus.smartcampus.model.Sensor;
import com.smartcampus.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.List;

/**
 * In-memory database using static lists to persist data while the server is running.
 * Following the university tutorial's MockDatabase pattern.
 */
public class MockDatabase {
    public static final List<Room> ROOMS = new ArrayList<>();
    public static final List<Sensor> SENSORS = new ArrayList<>();
    // Global list of readings, though filtered by sensorId in resources
    public static final List<SensorReading> READINGS = new ArrayList<>();

    static {
        // Initialise with some sample data for testing
        Room lib301 = new Room("LIB-301", "Library Quiet Study", 50);
        Room lab102 = new Room("LAB-102", "CS Computer Lab", 30);
        ROOMS.add(lib301);
        ROOMS.add(lab102);

        Sensor temp001 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        Sensor co2_001 = new Sensor("CO2-001", "CO2", "ACTIVE", 450.0, "LAB-102");
        SENSORS.add(temp001);
        SENSORS.add(co2_001);
        
        // Update rooms with sensor links
        lib301.addSensor("TEMP-001");
        lab102.addSensor("CO2-001");
    }
}
