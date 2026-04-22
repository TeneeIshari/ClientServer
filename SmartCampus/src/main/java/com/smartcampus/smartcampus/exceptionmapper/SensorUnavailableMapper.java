/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.exceptionmapper;

import com.smartcampus.smartcampus.exception.SmartCampusException.SensorUnavailableException;
import com.smartcampus.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps SensorUnavailableException to a 403 Forbidden response.
 * Handles Part 5.3 of the coursework.
 */
@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                403,
                "https://smartcampus.edu/api/docs/errors/403"
        );

        return Response.status(Status.FORBIDDEN)
                .entity(errorMessage)
                .build();
    }
}