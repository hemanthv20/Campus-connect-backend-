package com.socialmediaweb.socialmediaweb.dto;

public class UsernameCheckDTO {
    
    private boolean available;
    private String message;
    
    // Default constructor
    public UsernameCheckDTO() {}
    
    // Constructor with parameters
    public UsernameCheckDTO(boolean available, String message) {
        this.available = available;
        this.message = message;
    }
    
    // Getters and setters
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "UsernameCheckDTO{" +
                "available=" + available +
                ", message='" + message + '\'' +
                '}';
    }
}