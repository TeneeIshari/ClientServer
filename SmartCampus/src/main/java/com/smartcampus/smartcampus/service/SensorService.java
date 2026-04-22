package com.smartcampus.smartcampus.service;

import com.smartcampus.smartcampus.model.Sensor;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Sensor data.
 */
public class SensorService {
    private static final List<Sensor> sensors = new ArrayList<>();

    static {
        // Sample data
        sensors.add(new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301"));
        sensors.add(new Sensor("CO2-001", "CO2", "ACTIVE", 450.0, "LAB-102"));
    }

    public List<Sensor> getAllSensors() {
        return sensors;
    }

    public List<Sensor> getSensorsByType(String type) {
        return sensors.stream()
                .filter(s -> s.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public Sensor getSensorById(String id) {
        return sensors.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Sensor> getSensorsByRoomId(String roomId) {
        return sensors.stream()
                .filter(s -> s.getRoomId().equals(roomId))
                .collect(Collectors.toList());
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public boolean deleteSensor(String id) {
        return sensors.removeIf(s -> s.getId().equals(id));
    }
}
