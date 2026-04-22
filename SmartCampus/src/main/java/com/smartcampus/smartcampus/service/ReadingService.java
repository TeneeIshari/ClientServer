package com.smartcampus.smartcampus.service;

import com.smartcampus.smartcampus.model.SensorReading;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Sensor Reading data.
 */
public class ReadingService {
    private static final List<SensorReading> readings = new ArrayList<>();

    public List<SensorReading> getAllReadings() {
        return readings;
    }

    public List<SensorReading> getReadingsBySensorId(String sensorId) {
        return readings; 
    }

    public void addReading(SensorReading reading) {
        readings.add(reading);
    }
}
