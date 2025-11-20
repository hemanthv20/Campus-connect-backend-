package com.socialmediaweb.socialmediaweb.dto;

import java.util.Date;

public class UserInterestDTO {
    private Long id;
    private Long interestId;
    private String interestName;
    private String categoryName;
    private String description;
    private Integer passionLevel;
    private Boolean isFeatured;
    private Integer displayOrder;
    private Date createdAt;
    
    public UserInterestDTO() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getInterestId() { return interestId; }
    public void setInterestId(Long interestId) { this.interestId = interestId; }
    
    public String getInterestName() { return interestName; }
    public void setInterestName(String interestName) { this.interestName = interestName; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getPassionLevel() { return passionLevel; }
    public void setPassionLevel(Integer passionLevel) { this.passionLevel = passionLevel; }
    
    public Boolean getIsFeatured() { return isFeatured; }
    public void setIsFeatured(Boolean isFeatured) { this.isFeatured = isFeatured; }
    
    public Integer getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(Integer displayOrder) { this.displayOrder = displayOrder; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
