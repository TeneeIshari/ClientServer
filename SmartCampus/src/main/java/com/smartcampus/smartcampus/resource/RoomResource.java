package com.smartcampus.smartcampus.resource;

import com.smartcampus.smartcampus.model.ErrorMessage;
import com.smartcampus.smartcampus.model.Room;
import com.smartcampus.smartcampus.service.RoomService;
import com.smartcampus.smartcampus.service.SensorService;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for managing rooms.
 */
@Path("/rooms")
public class RoomResource {

    private RoomService roomService = new RoomService();
    private SensorService sensorService = new SensorService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Room> getAllRooms() {
        return roomService.getAllRooms();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoom(Room room) {
        roomService.addRoom(room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomService.getRoomById(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        // Business Logic Constraint: Cannot delete room if it has sensors
        if (!sensorService.getSensorsByRoomId(roomId).isEmpty()) {
            ErrorMessage errorMessage = new ErrorMessage(
                    "Room " + roomId + " cannot be deleted because it still contains active sensors.",
                    409,
                    "https://smartcampus.edu/api/docs/errors/409"
            );
            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }
        
        boolean deleted = roomService.deleteRoom(roomId);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
