package com.socialmediaweb.socialmediaweb.dto;

public class FollowResponse {
    private boolean success;
    private String message;
    private long followerCount;
    private long followingCount;
    
    // Default constructor
    public FollowResponse() {
    }
    
    // Constructor with all fields
    public FollowResponse(boolean success, String message, long followerCount, long followingCount) {
        this.success = success;
        this.message = message;
        this.followerCount = followerCount;
        this.followingCount = followingCount;
    }
    
    // Getters and setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getFollowerCount() {
        return followerCount;
    }
    
    public void setFollowerCount(long followerCount) {
        this.followerCount = followerCount;
    }
    
    public long getFollowingCount() {
        return followingCount;
    }
    
    public void setFollowingCount(long followingCount) {
        this.followingCount = followingCount;
    }
}
