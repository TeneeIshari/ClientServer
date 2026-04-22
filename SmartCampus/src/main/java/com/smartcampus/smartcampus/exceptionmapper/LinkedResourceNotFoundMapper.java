/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.exceptionmapper;

import com.smartcampus.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.smartcampus.model.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps LinkedResourceNotFoundException to a 422 Unprocessable Entity response.
 * Handles Part 5.2 of the coursework.
 */
@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException exception) {
        ErrorMessage errorMessage = new ErrorMessage(
                exception.getMessage(),
                422,
                "https://smartcampus.edu/api/docs/errors/422"
        );

        // 422 Unprocessable Entity is semantically accurate for missing references in payload
        return Response.status(422)
                .entity(errorMessage)
                .build();
    }
}