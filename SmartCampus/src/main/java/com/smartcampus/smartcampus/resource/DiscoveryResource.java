/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Root discovery endpoint providing API metadata and resource links.
 * Handles Part 1.2 of the coursework.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DiscoveryResource {

    @GET
    public Map<String, Object> getDiscovery() {
        Map<String, Object> discovery = new HashMap<>();
        discovery.put("version", "1.0.0-v1");
        discovery.put("description", "Smart Campus Sensor & Room Management API");
        discovery.put("adminContact", "admin@smartcampus.edu");

        Map<String, String> links = new HashMap<>();
        links.put("rooms", "/api/v1/rooms");
        links.put("sensors", "/api/v1/sensors");
        discovery.put("links", links);

        return discovery;
    }
}