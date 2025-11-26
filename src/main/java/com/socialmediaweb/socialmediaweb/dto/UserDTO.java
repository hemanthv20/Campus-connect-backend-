package com.socialmediaweb.socialmediaweb.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String profilePicture;
    private String college;
    private String semester;
    private String batch;
    private Boolean admin;
    private LocalDateTime createdAt;
    private boolean isFollowing;      // Does current user follow this user?
    private boolean isFollower;       // Does this user follow current user?
    private boolean isMutualFollow;   // Mutual follow status
    
    // Default constructor
    public UserDTO() {
    }
    
    // Constructor with basic fields
    public UserDTO(Long userId, String username, String firstName, String lastName, 
                   String profilePicture, String college) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePicture = profilePicture;
        this.college = college;
    }
    
    // Getters and setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSemester() {
        return semester;
    }
    
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    public String getBatch() {
        return batch;
    }
    
    public void setBatch(String batch) {
        this.batch = batch;
    }
    
    public Boolean getAdmin() {
        return admin;
    }
    
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
