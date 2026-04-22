/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampus.model;

/**
 * Base interface for all models to enable the Generic DAO pattern.
 * Using String IDs to satisfy the Smart Campus coursework specification.
 */
public interface BaseModel {
    String getId();
    void setId(String id);
}
