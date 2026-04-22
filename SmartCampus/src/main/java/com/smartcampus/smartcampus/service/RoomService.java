package com.smartcampus.smartcampus.service;

import com.smartcampus.smartcampus.model.Room;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing Room data.
 */
public class RoomService {
    private static final List<Room> rooms = new ArrayList<>();

    static {
        // Sample data
        rooms.add(new Room("LIB-301", "Library Quiet Study", 50));
        rooms.add(new Room("LAB-102", "CS Computer Lab", 30));
    }

    public List<Room> getAllRooms() {
        return rooms;
    }

    public Room getRoomById(String id) {
        return rooms.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public boolean deleteRoom(String id) {
        return rooms.removeIf(r -> r.getId().equals(id));
    }
}
