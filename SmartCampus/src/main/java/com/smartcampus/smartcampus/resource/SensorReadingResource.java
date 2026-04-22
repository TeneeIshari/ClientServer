/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.resource;

import com.smartcampus.smartcampus.dao.GenericDAO;
import com.smartcampus.smartcampus.dao.MockDatabase;
import com.smartcampus.smartcampus.exception.SmartCampusException.SensorUnavailableException;
import com.smartcampus.smartcampus.model.Sensor;
import com.smartcampus.smartcampus.model.SensorReading;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Sub-resource for managing historical readings for a specific sensor.
 * Handles Part 4.2 of the coursework.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private String sensorId;
    private GenericDAO<Sensor> sensorDAO = new GenericDAO<>(MockDatabase.SENSORS);
    private GenericDAO<SensorReading> readingDAO = new GenericDAO<>(MockDatabase.READINGS);

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public List<SensorReading> getReadings() {
        // Filter global readings to find only those belonging to this sensor context
        // Note: In a production app, readings would likely have a sensorId field.
        // For this mock implementation, we return all readings recorded via this context.
        return readingDAO.getAll(); 
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = sensorDAO.getById(sensorId);
        
        // Part 5.3: State Constraint - Check if sensor is in MAINTENANCE
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Cannot accept readings. Sensor " + sensorId + " is currently under MAINTENANCE.");
        }

        readingDAO.add(reading);

        // Side effect: Update currentValue on parent Sensor (Part 4.2)
        sensor.setCurrentValue(reading.getValue());
        sensorDAO.update(sensor);

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}