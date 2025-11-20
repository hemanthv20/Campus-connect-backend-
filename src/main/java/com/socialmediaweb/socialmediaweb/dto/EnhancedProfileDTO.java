package com.socialmediaweb.socialmediaweb.dto;

import java.util.List;

public class EnhancedProfileDTO {
    private UserDTO user;
    private String bio;
    private String phone;
    private String location;
    private String availabilityStatus;
    private List<String> lookingFor;
    private List<String> availableFor;
    private Integer profileCompletionPercentage;
    private List<UserSkillDTO> skills;
    private List<UserInterestDTO> interests;
    private List<GoalDTO> goals;
    private List<ProjectDTO> projects;
    private List<ExperienceDTO> experiences;
    private List<EducationDTO> education;
    private List<SocialLinkDTO> socialLinks;
    
    public EnhancedProfileDTO() {}
    
    // Getters and Setters
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    
    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    
    public List<String> getLookingFor() { return lookingFor; }
    public void setLookingFor(List<String> lookingFor) { this.lookingFor = lookingFor; }
    
    public List<String> getAvailableFor() { return availableFor; }
    public void setAvailableFor(List<String> availableFor) { this.availableFor = availableFor; }
    
    public Integer getProfileCompletionPercentage() { return profileCompletionPercentage; }
    public void setProfileCompletionPercentage(Integer profileCompletionPercentage) { 
        this.profileCompletionPercentage = profileCompletionPercentage; 
    }
    
    public List<UserSkillDTO> getSkills() { return skills; }
    public void setSkills(List<UserSkillDTO> skills) { this.skills = skills; }
    
    public List<UserInterestDTO> getInterests() { return interests; }
    public void setInterests(List<UserInterestDTO> interests) { this.interests = interests; }
    
    public List<GoalDTO> getGoals() { return goals; }
    public void setGoals(List<GoalDTO> goals) { this.goals = goals; }
    
    public List<ProjectDTO> getProjects() { return projects; }
    public void setProjects(List<ProjectDTO> projects) { this.projects = projects; }
    
    public List<ExperienceDTO> getExperiences() { return experiences; }
    public void setExperiences(List<ExperienceDTO> experiences) { this.experiences = experiences; }
    
    public List<EducationDTO> getEducation() { return education; }
    public void setEducation(List<EducationDTO> education) { this.education = education; }
    
    public List<SocialLinkDTO> getSocialLinks() { return socialLinks; }
    public void setSocialLinks(List<SocialLinkDTO> socialLinks) { this.socialLinks = socialLinks; }
}
