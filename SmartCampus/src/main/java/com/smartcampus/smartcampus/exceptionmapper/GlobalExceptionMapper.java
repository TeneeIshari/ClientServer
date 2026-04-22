/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.exceptionmapper;


import com.smartcampus.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Global catch-all ExceptionMapper for internal server errors.
 * Ensures no stack traces are leaked to the client.
 * Handles Part 5.4 of the coursework.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log the actual error for the administrator
        LOGGER.log(Level.SEVERE, "Unexpected error occurred", exception);

        ErrorMessage errorMessage = new ErrorMessage(
                "An unexpected internal server error occurred. Please contact the administrator.",
                500,
                "https://smartcampus.edu/api/docs/errors/500"
        );

        return Response.status(Status.INTERNAL_SERVER_ERROR)
                .entity(errorMessage)
                .build();
    }
}