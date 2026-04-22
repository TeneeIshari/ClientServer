/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.resource;

import com.smartcampus.smartcampus.dao.GenericDAO;
import com.smartcampus.smartcampus.dao.MockDatabase;
import com.smartcampus.smartcampus.exception.SmartCampusException.DataNotFoundException;
import com.smartcampus.smartcampus.exception.SmartCampusException.RoomNotEmptyException;
import com.smartcampus.smartcampus.model.Room;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST resource for managing Room entities.

 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private GenericDAO<Room> roomDAO = new GenericDAO<>(MockDatabase.ROOMS);

    @GET
    public List<Room> getAllRooms() {
        return roomDAO.getAll();
    }

    @POST
    public Response createRoom(Room room) {
        roomDAO.add(room);
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoomById(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getById(roomId);
        if (room == null) {
            throw new DataNotFoundException("Room with ID " + roomId + " not found.");
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = roomDAO.getById(roomId);
        if (room == null) {
            throw new DataNotFoundException("Room with ID " + roomId + " not found.");
        }

        // Part 2.2: Safety Logic - Prevent deletion if room has sensors
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete Room " + roomId + " because it contains active sensors.");
        }

        roomDAO.delete(roomId);
        return Response.noContent().build();
    }
}
