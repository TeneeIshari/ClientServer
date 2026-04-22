/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.resource;


import com.smartcampus.smartcampus.dao.GenericDAO;
import com.smartcampus.smartcampus.dao.MockDatabase;
import com.smartcampus.smartcampus.exception.SmartCampusException.DataNotFoundException;
import com.smartcampus.smartcampus.exception.SmartCampusException.LinkedResourceNotFoundException;
import com.smartcampus.smartcampus.model.Room;
import com.smartcampus.smartcampus.model.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST resource for managing Sensor entities.
 * Handles Part 3 and Part 4.1 (Sub-resource locator) of the coursework.
 */
@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private GenericDAO<Sensor> sensorDAO = new GenericDAO<>(MockDatabase.SENSORS);
    private GenericDAO<Room> roomDAO = new GenericDAO<>(MockDatabase.ROOMS);

    @GET
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        List<Sensor> allSensors = sensorDAO.getAll();
        
        // Part 3.2: Filtered Retrieval & Search
        if (type != null && !type.isEmpty()) {
            return allSensors.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return allSensors;
    }

    @POST
    public Response registerSensor(Sensor sensor) {
        // Part 3.1: Sensor Resource & Integrity - Verify Room exists
        Room room = roomDAO.getById(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Cannot register sensor. Room ID " + sensor.getRoomId() + " does not exist.");
        }

        sensorDAO.add(sensor);
        
        // Side effect: Link sensor to room
        room.addSensor(sensor.getId());
        
        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorDAO.getById(sensorId);
        if (sensor == null) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }
        return sensor;
    }

    /**
     * Part 4.1: Sub-Resource Locator Pattern.
     * Delegates historical reading operations to SensorReadingResource.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        // Verify sensor exists before delegating
        if (sensorDAO.getById(sensorId) == null) {
            throw new DataNotFoundException("Sensor with ID " + sensorId + " not found.");
        }
        return new SensorReadingResource(sensorId);
    }
}