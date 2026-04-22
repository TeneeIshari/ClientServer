/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.exceptionmapper;
import com.smartcampus.smartcampus.exception.SmartCampusException.DataNotFoundException;
import com.smartcampus.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps DataNotFoundException to a 404 Not Found response.
 * Follows Tutorial 09, Step 3.
 */
@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

    @Override
    public Response toResponse(DataNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                404,
                "https://smartcampus.edu/api/docs/errors/404"
        );

        return Response.status(Status.NOT_FOUND)
                .entity(errorMessage)
                .build();
    }
}
