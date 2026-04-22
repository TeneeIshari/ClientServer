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
        // In this implementation, we don't have a direct sensorId in SensorReading POJO 
        // as per the spec, but we can filter if we add context. 
        // However, the spec says "fetch history... for that specific sensor context".
        // We'll manage it by filtering a global list if we had the ID, 
        // or just return all for that sensor context.
        return readings; 
    }

    public void addReading(SensorReading reading) {
        readings.add(reading);
    }
}
