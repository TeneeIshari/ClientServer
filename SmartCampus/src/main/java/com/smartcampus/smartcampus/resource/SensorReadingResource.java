package com.smartcampus.smartcampus.resource;

import com.smartcampus.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.smartcampus.model.Sensor;
import com.smartcampus.smartcampus.model.SensorReading;
import com.smartcampus.smartcampus.service.ReadingService;
import com.smartcampus.smartcampus.service.SensorService;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Sub-resource for handling readings for a specific sensor.
 */
public class SensorReadingResource {

    private String sensorId;
    private SensorService sensorService = new SensorService();
    private ReadingService readingService = new ReadingService();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SensorReading> getHistory() {
        return readingService.getReadingsBySensorId(sensorId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        Sensor sensor = sensorService.getSensorById(sensorId);
        
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // State Constraint: Disconnect if in MAINTENANCE
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor " + sensorId + " is in MAINTENANCE and cannot accept new readings.");
        }

        // Side Effect: Update parent sensor's current value
        sensor.setCurrentValue(reading.getValue());
        
        readingService.addReading(reading);
        return Response.status(Response.Status.CREATED).entity(reading).build();
    }
}