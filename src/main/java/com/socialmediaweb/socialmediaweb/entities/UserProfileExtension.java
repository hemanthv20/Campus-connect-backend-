package com.socialmediaweb.socialmediaweb.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_profile_extensions")
public class UserProfileExtension {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Users user;
    
    @Column(name = "availability_status", length = 20)
    private String availabilityStatus = "Available";
    
    @Column(name = "looking_for", columnDefinition = "TEXT")
    private String lookingFor;
    
    @Column(name = "available_for", columnDefinition = "TEXT")
    private String availableFor;
    
    @Column(length = 50)
    private String timezone;
    
    @Column(name = "preferred_contact_method", length = 50)
    private String preferredContactMethod;
    
    @Column(name = "response_time", length = 50)
    private String responseTime;
    
    @Column(name = "profile_completion_percentage")
    private Integer profileCompletionPercentage = 0;
    
    @Column(name = "last_active")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastActive = new Date();
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
    
    public UserProfileExtension() {}
    
    public UserProfileExtension(Users user) {
        this.user = user;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Users getUser() { return user; }
    public void setUser(Users user) { this.user = user; }
    
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { 
        this.availabilityStatus = availabilityStatus; 
    }
    
    public String getLookingFor() { return lookingFor; }
    public void setLookingFor(String lookingFor) { this.lookingFor = lookingFor; }
    
    public String getAvailableFor() { return availableFor; }
    public void setAvailableFor(String availableFor) { this.availableFor = availableFor; }
    
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    
    public String getPreferredContactMethod() { return preferredContactMethod; }
    public void setPreferredContactMethod(String preferredContactMethod) { 
        this.preferredContactMethod = preferredContactMethod; 
    }
    
    public String getResponseTime() { return responseTime; }
    public void setResponseTime(String responseTime) { this.responseTime = responseTime; }
    
    public Integer getProfileCompletionPercentage() { return profileCompletionPercentage; }
    public void setProfileCompletionPercentage(Integer profileCompletionPercentage) { 
        this.profileCompletionPercentage = profileCompletionPercentage; 
    }
    
    public Date getLastActive() { return lastActive; }
    public void setLastActive(Date lastActive) { this.lastActive = lastActive; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
