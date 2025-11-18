package com.socialmediaweb.socialmediaweb.dto;

public class FollowCountsDTO {
    private long followers;
    private long following;
    
    // Default constructor
    public FollowCountsDTO() {
    }
    
    // Constructor with all fields
    public FollowCountsDTO(long followers, long following) {
        this.followers = followers;
        this.following = following;
    }
    
    // Getters and setters
    public long getFollowers() {
        return followers;
    }
    
    public void setFollowers(long followers) {
        this.followers = followers;
    }
    
    public long getFollowing() {
        return following;
    }
    
    public void setFollowing(long following) {
        this.following = following;
    }
}
