package com.socialmediaweb.socialmediaweb.dto;

public class UserDTO {
    private int userId;
    private String username;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String college;
    private boolean isFollowing;      // Does current user follow this user?
    private boolean isFollower;       // Does this user follow current user?
    private boolean isMutualFollow;   // Mutual follow status
    
    // Default constructor
    public UserDTO() {
    }
    
    // Constructor with basic fields
    public UserDTO(int userId, String username, String firstName, String lastName, 
                   String profilePicture, String college) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.college = college;
    }
    
    // Getters and setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public String getCollege() {
        return college;
    }
    
    public void setCollege(String college) {
        this.college = college;
    }
    
    public boolean isFollowing() {
        return isFollowing;
    }
    
    public void setFollowing(boolean following) {
        isFollowing = following;
    }
    
    public boolean isFollower() {
        return isFollower;
    }
    
    public void setFollower(boolean follower) {
        isFollower = follower;
    }
    
    public boolean isMutualFollow() {
        return isMutualFollow;
    }
    
    public void setMutualFollow(boolean mutualFollow) {
        isMutualFollow = mutualFollow;
    }
}
