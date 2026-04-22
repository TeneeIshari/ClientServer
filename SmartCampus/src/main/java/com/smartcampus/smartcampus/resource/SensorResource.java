package com.smartcampus.smartcampus.resource;

import com.smartcampus.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.smartcampus.model.Sensor;
import com.smartcampus.smartcampus.service.RoomService;
import com.smartcampus.smartcampus.service.SensorService;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for managing sensors.
 */
@Path("/sensors")
public class SensorResource {

    private final SensorService sensorService = new SensorService();
    private final RoomService roomService = new RoomService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sensor> getSensors(@QueryParam("type") String type) {
        if (type != null && !type.isEmpty()) {
            return sensorService.getSensorsByType(type);
        }
        return sensorService.getAllSensors();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerSensor(Sensor sensor) {
        // Verification: Check if the roomId specified exists
        if (roomService.getRoomById(sensor.getRoomId()) == null) {
            throw new LinkedResourceNotFoundException("Cannot register sensor. Room ID " + sensor.getRoomId() + " does not exist.");
        }
        
        sensorService.addSensor(sensor);
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    @GET
    @Path("/{sensorId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensorService.getSensorById(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        boolean deleted = sensorService.deleteSensor(sensorId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    /**
     * Sub-Resource Locator for sensor readings.
     */
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}