package com.socialmediaweb.socialmediaweb.service;

import com.socialmediaweb.socialmediaweb.dto.*;
import com.socialmediaweb.socialmediaweb.entities.*;
import com.socialmediaweb.socialmediaweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    
    @Autowired
    private UserSkillRepository userSkillRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private UserInterestRepository userInterestRepository;
    
    @Autowired
    private InterestRepository interestRepository;
    
    @Autowired
    private GoalRepository goalRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private ExperienceRepository experienceRepository;
    
    @Autowired
    private EducationRepository educationRepository;
    
    @Autowired
    private SocialLinkRepository socialLinkRepository;
    
    @Autowired
    private UserProfileExtensionRepository profileExtensionRepository;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
    // ==================== SKILLS ====================
    
    public List<UserSkillDTO> getUserSkills(int userId) {
        List<UserSkill> userSkills = userSkillRepository.findByUserIdOrderByDisplayOrder((long) userId);
        return userSkills.stream().map(this::convertToUserSkillDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public UserSkillDTO addUserSkill(int userId, UserSkillDTO skillDTO, Users user) {
        Skill skill = skillRepository.findById(skillDTO.getSkillId())
            .orElseThrow(() -> new RuntimeException("Skill not found"));
        
        UserSkill userSkill = new UserSkill();
        userSkill.setUser(user);
        userSkill.setSkill(skill);
        userSkill.setProficiencyLevel(skillDTO.getProficiencyLevel());
        userSkill.setYearsExperience(skillDTO.getYearsExperience());
        userSkill.setCertificationUrl(skillDTO.getCertificationUrl());
        userSkill.setIsFeatured(skillDTO.getIsFeatured() != null ? skillDTO.getIsFeatured() : false);
        userSkill.setDisplayOrder(skillDTO.getDisplayOrder() != null ? skillDTO.getDisplayOrder() : 0);
        
        userSkill = userSkillRepository.save(userSkill);
        return convertToUserSkillDTO(userSkill);
    }
    
    @Transactional
    public UserSkillDTO updateUserSkill(Long userSkillId, UserSkillDTO skillDTO) {
        UserSkill userSkill = userSkillRepository.findById(userSkillId)
            .orElseThrow(() -> new RuntimeException("User skill not found"));
        
        if (skillDTO.getProficiencyLevel() != null) {
            userSkill.setProficiencyLevel(skillDTO.getProficiencyLevel());
        }
        if (skillDTO.getYearsExperience() != null) {
            userSkill.setYearsExperience(skillDTO.getYearsExperience());
        }
        if (skillDTO.getCertificationUrl() != null) {
            userSkill.setCertificationUrl(skillDTO.getCertificationUrl());
        }
        if (skillDTO.getIsFeatured() != null) {
            userSkill.setIsFeatured(skillDTO.getIsFeatured());
        }
        if (skillDTO.getDisplayOrder() != null) {
            userSkill.setDisplayOrder(skillDTO.getDisplayOrder());
        }
        
        userSkill.setUpdatedAt(new Date());
        userSkill = userSkillRepository.save(userSkill);
        return convertToUserSkillDTO(userSkill);
    }
    
    @Transactional
    public void deleteUserSkill(int userId, Long skillId) {
        userSkillRepository.deleteByUserUserIdAndSkillId((long) userId, skillId);
    }
    
    public List<SkillDTO> searchSkills(String query) {
        List<Skill> skills = skillRepository.searchByName(query);
        return skills.stream().map(this::convertToSkillDTO).collect(Collectors.toList());
    }
    
    public List<SkillDTO> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return skills.stream().map(this::convertToSkillDTO).collect(Collectors.toList());
    }
    
    // ==================== INTERESTS ====================
    
    public List<UserInterestDTO> getUserInterests(int userId) {
        List<UserInterest> userInterests = userInterestRepository.findByUserIdOrderByDisplayOrder((long) userId);
        return userInterests.stream().map(this::convertToUserInterestDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public UserInterestDTO addUserInterest(int userId, UserInterestDTO interestDTO, Users user) {
        Interest interest = interestRepository.findById(interestDTO.getInterestId())
            .orElseThrow(() -> new RuntimeException("Interest not found"));
        
        UserInterest userInterest = new UserInterest();
        userInterest.setUser(user);
        userInterest.setInterest(interest);
        userInterest.setDescription(interestDTO.getDescription());
        userInterest.setPassionLevel(interestDTO.getPassionLevel() != null ? interestDTO.getPassionLevel() : 3);
        userInterest.setIsFeatured(interestDTO.getIsFeatured() != null ? interestDTO.getIsFeatured() : false);
        userInterest.setDisplayOrder(interestDTO.getDisplayOrder() != null ? interestDTO.getDisplayOrder() : 0);
        
        userInterest = userInterestRepository.save(userInterest);
        return convertToUserInterestDTO(userInterest);
    }
    
    @Transactional
    public void deleteUserInterest(int userId, Long interestId) {
        userInterestRepository.deleteByUserUserIdAndInterestId((long) userId, interestId);
    }
    
    public List<InterestDTO> searchInterests(String query) {
        List<Interest> interests = interestRepository.searchByName(query);
        return interests.stream().map(this::convertToInterestDTO).collect(Collectors.toList());
    }
    
    public List<InterestDTO> getAllInterests() {
        List<Interest> interests = interestRepository.findAll();
        return interests.stream().map(this::convertToInterestDTO).collect(Collectors.toList());
    }
    
    // ==================== GOALS ====================
    
    public List<GoalDTO> getUserGoals(int userId) {
        List<Goal> goals = goalRepository.findByUserIdOrderByPriority(userId);
        return goals.stream().map(this::convertToGoalDTO).collect(Collectors.toList());
    }
    
    public GoalDTO getGoalById(Long goalId) {
        Goal goal = goalRepository.findById(goalId)
            .orElseThrow(() -> new RuntimeException("Goal not found"));
        return convertToGoalDTO(goal);
    }
    
    @Transactional
    public GoalDTO createGoal(int userId, GoalDTO goalDTO, Users user) {
        Goal goal = new Goal();
        goal.setUser(user);
        goal.setTitle(goalDTO.getTitle());
        goal.setDescription(goalDTO.getDescription());
        goal.setCategory(goalDTO.getCategory());
        goal.setTargetDate(goalDTO.getTargetDate());
        goal.setStatus(goalDTO.getStatus() != null ? goalDTO.getStatus() : "Not Started");
        goal.setProgressPercentage(goalDTO.getProgressPercentage() != null ? goalDTO.getProgressPercentage() : 0);
        goal.setIsPublic(goalDTO.getIsPublic() != null ? goalDTO.getIsPublic() : true);
        goal.setPriorityLevel(goalDTO.getPriorityLevel() != null ? goalDTO.getPriorityLevel() : 3);
        
        goal = goalRepository.save(goal);
        return convertToGoalDTO(goal);
    }
    
    @Transactional
    public GoalDTO updateGoal(Long goalId, GoalDTO goalDTO) {
        Goal goal = goalRepository.findById(goalId)
            .orElseThrow(() -> new RuntimeException("Goal not found"));
        
        if (goalDTO.getTitle() != null) goal.setTitle(goalDTO.getTitle());
        if (goalDTO.getDescription() != null) goal.setDescription(goalDTO.getDescription());
        if (goalDTO.getCategory() != null) goal.setCategory(goalDTO.getCategory());
        if (goalDTO.getTargetDate() != null) goal.setTargetDate(goalDTO.getTargetDate());
        if (goalDTO.getStatus() != null) {
            goal.setStatus(goalDTO.getStatus());
            if ("Completed".equals(goalDTO.getStatus())) {
                goal.setCompletedAt(new Date());
                goal.setProgressPercentage(100);
            }
        }
        if (goalDTO.getProgressPercentage() != null) goal.setProgressPercentage(goalDTO.getProgressPercentage());
        if (goalDTO.getIsPublic() != null) goal.setIsPublic(goalDTO.getIsPublic());
        if (goalDTO.getPriorityLevel() != null) goal.setPriorityLevel(goalDTO.getPriorityLevel());
        
        goal.setUpdatedAt(new Date());
        goal = goalRepository.save(goal);
        return convertToGoalDTO(goal);
    }
    
    @Transactional
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }
    
    // ==================== PROJECTS ====================
    
    public List<ProjectDTO> getUserProjects(int userId) {
        List<Project> projects = projectRepository.findByUserIdOrderByDisplayOrder(userId);
        return projects.stream().map(this::convertToProjectDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public ProjectDTO createProject(int userId, ProjectDTO projectDTO, Users user) {
        Project project = new Project();
        project.setUser(user);
        project.setTitle(projectDTO.getTitle());
        project.setDescription(projectDTO.getDescription());
        project.setTechnologies(convertListToJson(projectDTO.getTechnologies()));
        project.setGithubUrl(projectDTO.getGithubUrl());
        project.setDemoUrl(projectDTO.getDemoUrl());
        project.setImageUrls(convertListToJson(projectDTO.getImageUrls()));
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatus(projectDTO.getStatus() != null ? projectDTO.getStatus() : "In Progress");
        project.setIsFeatured(projectDTO.getIsFeatured() != null ? projectDTO.getIsFeatured() : false);
        project.setDisplayOrder(projectDTO.getDisplayOrder() != null ? projectDTO.getDisplayOrder() : 0);
        
        project = projectRepository.save(project);
        return convertToProjectDTO(project);
    }
    
    @Transactional
    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        if (projectDTO.getTitle() != null) project.setTitle(projectDTO.getTitle());
        if (projectDTO.getDescription() != null) project.setDescription(projectDTO.getDescription());
        if (projectDTO.getTechnologies() != null) project.setTechnologies(convertListToJson(projectDTO.getTechnologies()));
        if (projectDTO.getGithubUrl() != null) project.setGithubUrl(projectDTO.getGithubUrl());
        if (projectDTO.getDemoUrl() != null) project.setDemoUrl(projectDTO.getDemoUrl());
        if (projectDTO.getImageUrls() != null) project.setImageUrls(convertListToJson(projectDTO.getImageUrls()));
        if (projectDTO.getStartDate() != null) project.setStartDate(projectDTO.getStartDate());
        if (projectDTO.getEndDate() != null) project.setEndDate(projectDTO.getEndDate());
        if (projectDTO.getStatus() != null) project.setStatus(projectDTO.getStatus());
        if (projectDTO.getIsFeatured() != null) project.setIsFeatured(projectDTO.getIsFeatured());
        if (projectDTO.getDisplayOrder() != null) project.setDisplayOrder(projectDTO.getDisplayOrder());
        
        project.setUpdatedAt(new Date());
        project = projectRepository.save(project);
        return convertToProjectDTO(project);
    }
    
    @Transactional
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }
    
    // ==================== EXPERIENCE ====================
    
    public List<ExperienceDTO> getUserExperiences(int userId) {
        List<Experience> experiences = experienceRepository.findByUserIdOrderByDisplayOrder(userId);
        return experiences.stream().map(this::convertToExperienceDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public ExperienceDTO createExperience(int userId, ExperienceDTO expDTO, Users user) {
        Experience experience = new Experience();
        experience.setUser(user);
        experience.setTitle(expDTO.getTitle());
        experience.setCompanyOrganization(expDTO.getCompanyOrganization());
        experience.setExperienceType(expDTO.getExperienceType());
        experience.setDescription(expDTO.getDescription());
        experience.setStartDate(expDTO.getStartDate());
        experience.setEndDate(expDTO.getEndDate());
        experience.setIsCurrent(expDTO.getIsCurrent() != null ? expDTO.getIsCurrent() : false);
        experience.setLocation(expDTO.getLocation());
        experience.setAchievements(expDTO.getAchievements());
        experience.setDisplayOrder(expDTO.getDisplayOrder() != null ? expDTO.getDisplayOrder() : 0);
        
        experience = experienceRepository.save(experience);
        return convertToExperienceDTO(experience);
    }
    
    @Transactional
    public void deleteExperience(Long experienceId) {
        experienceRepository.deleteById(experienceId);
    }
    
    // ==================== EDUCATION ====================
    
    public List<EducationDTO> getUserEducation(int userId) {
        List<Education> education = educationRepository.findByUserIdOrderByDisplayOrder(userId);
        return education.stream().map(this::convertToEducationDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public EducationDTO createEducation(int userId, EducationDTO eduDTO, Users user) {
        Education education = new Education();
        education.setUser(user);
        education.setInstitutionName(eduDTO.getInstitutionName());
        education.setDegree(eduDTO.getDegree());
        education.setFieldOfStudy(eduDTO.getFieldOfStudy());
        education.setStartDate(eduDTO.getStartDate());
        education.setEndDate(eduDTO.getEndDate());
        education.setGpa(eduDTO.getGpa());
        education.setIsCurrent(eduDTO.getIsCurrent() != null ? eduDTO.getIsCurrent() : false);
        education.setAchievements(eduDTO.getAchievements());
        education.setRelevantCoursework(eduDTO.getRelevantCoursework());
        education.setDisplayOrder(eduDTO.getDisplayOrder() != null ? eduDTO.getDisplayOrder() : 0);
        
        education = educationRepository.save(education);
        return convertToEducationDTO(education);
    }
    
    @Transactional
    public void deleteEducation(Long educationId) {
        educationRepository.deleteById(educationId);
    }
    
    // ==================== SOCIAL LINKS ====================
    
    public List<SocialLinkDTO> getUserSocialLinks(int userId) {
        List<SocialLink> socialLinks = socialLinkRepository.findByUserIdOrderByDisplayOrder(userId);
        return socialLinks.stream().map(this::convertToSocialLinkDTO).collect(Collectors.toList());
    }
    
    @Transactional
    public SocialLinkDTO addSocialLink(int userId, SocialLinkDTO linkDTO, Users user) {
        SocialLink socialLink = new SocialLink();
        socialLink.setUser(user);
        socialLink.setPlatform(linkDTO.getPlatform());
        socialLink.setUrl(linkDTO.getUrl());
        socialLink.setDisplayOrder(linkDTO.getDisplayOrder() != null ? linkDTO.getDisplayOrder() : 0);
        
        socialLink = socialLinkRepository.save(socialLink);
        return convertToSocialLinkDTO(socialLink);
    }
    
    @Transactional
    public void deleteSocialLink(Long linkId) {
        socialLinkRepository.deleteById(linkId);
    }
    
    // ==================== CONVERSION METHODS ====================
    
    private UserSkillDTO convertToUserSkillDTO(UserSkill userSkill) {
        UserSkillDTO dto = new UserSkillDTO();
        dto.setId(userSkill.getId());
        dto.setSkillId(userSkill.getSkill().getId());
        dto.setSkillName(userSkill.getSkill().getName());
        dto.setCategoryName(userSkill.getSkill().getCategory() != null ? 
            userSkill.getSkill().getCategory().getName() : null);
        dto.setProficiencyLevel(userSkill.getProficiencyLevel());
        dto.setYearsExperience(userSkill.getYearsExperience());
        dto.setCertificationUrl(userSkill.getCertificationUrl());
        dto.setIsFeatured(userSkill.getIsFeatured());
        dto.setDisplayOrder(userSkill.getDisplayOrder());
        dto.setEndorsementCount(userSkill.getEndorsements() != null ? userSkill.getEndorsements().size() : 0);
        dto.setCreatedAt(userSkill.getCreatedAt());
        return dto;
    }
    
    private SkillDTO convertToSkillDTO(Skill skill) {
        SkillDTO dto = new SkillDTO();
        dto.setId(skill.getId());
        dto.setName(skill.getName());
        dto.setCategoryName(skill.getCategory() != null ? skill.getCategory().getName() : null);
        dto.setDescription(skill.getDescription());
        dto.setIsVerified(skill.getIsVerified());
        dto.setUsageCount(skill.getUsageCount());
        return dto;
    }
    
    private UserInterestDTO convertToUserInterestDTO(UserInterest userInterest) {
        UserInterestDTO dto = new UserInterestDTO();
        dto.setId(userInterest.getId());
        dto.setInterestId(userInterest.getInterest().getId());
        dto.setInterestName(userInterest.getInterest().getName());
        dto.setCategoryName(userInterest.getInterest().getCategory() != null ? 
            userInterest.getInterest().getCategory().getName() : null);
        dto.setDescription(userInterest.getDescription());
        dto.setPassionLevel(userInterest.getPassionLevel());
        dto.setIsFeatured(userInterest.getIsFeatured());
        dto.setDisplayOrder(userInterest.getDisplayOrder());
        dto.setCreatedAt(userInterest.getCreatedAt());
        return dto;
    }
    
    private InterestDTO convertToInterestDTO(Interest interest) {
        InterestDTO dto = new InterestDTO();
        dto.setId(interest.getId());
        dto.setName(interest.getName());
        dto.setCategoryName(interest.getCategory() != null ? interest.getCategory().getName() : null);
        dto.setDescription(interest.getDescription());
        dto.setUsageCount(interest.getUsageCount());
        return dto;
    }
    
    private GoalDTO convertToGoalDTO(Goal goal) {
        GoalDTO dto = new GoalDTO();
        dto.setId(goal.getId());
        dto.setUserId(goal.getUser().getUserId());
        dto.setTitle(goal.getTitle());
        dto.setDescription(goal.getDescription());
        dto.setCategory(goal.getCategory());
        dto.setTargetDate(goal.getTargetDate());
        dto.setStatus(goal.getStatus());
        dto.setProgressPercentage(goal.getProgressPercentage());
        dto.setIsPublic(goal.getIsPublic());
        dto.setPriorityLevel(goal.getPriorityLevel());
        dto.setCreatedAt(goal.getCreatedAt());
        dto.setUpdatedAt(goal.getUpdatedAt());
        dto.setCompletedAt(goal.getCompletedAt());
        return dto;
    }
    
    private ProjectDTO convertToProjectDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setUserId(project.getUser().getUserId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setTechnologies(convertJsonToList(project.getTechnologies()));
        dto.setGithubUrl(project.getGithubUrl());
        dto.setDemoUrl(project.getDemoUrl());
        dto.setImageUrls(convertJsonToList(project.getImageUrls()));
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());
        dto.setIsFeatured(project.getIsFeatured());
        dto.setDisplayOrder(project.getDisplayOrder());
        dto.setCreatedAt(project.getCreatedAt());
        return dto;
    }
    
    private ExperienceDTO convertToExperienceDTO(Experience experience) {
        ExperienceDTO dto = new ExperienceDTO();
        dto.setId(experience.getId());
        dto.setTitle(experience.getTitle());
        dto.setCompanyOrganization(experience.getCompanyOrganization());
        dto.setExperienceType(experience.getExperienceType());
        dto.setDescription(experience.getDescription());
        dto.setStartDate(experience.getStartDate());
        dto.setEndDate(experience.getEndDate());
        dto.setIsCurrent(experience.getIsCurrent());
        dto.setLocation(experience.getLocation());
        dto.setAchievements(experience.getAchievements());
        dto.setDisplayOrder(experience.getDisplayOrder());
        return dto;
    }
    
    private EducationDTO convertToEducationDTO(Education education) {
        EducationDTO dto = new EducationDTO();
        dto.setId(education.getId());
        dto.setInstitutionName(education.getInstitutionName());
        dto.setDegree(education.getDegree());
        dto.setFieldOfStudy(education.getFieldOfStudy());
        dto.setStartDate(education.getStartDate());
        dto.setEndDate(education.getEndDate());
        dto.setGpa(education.getGpa());
        dto.setIsCurrent(education.getIsCurrent());
        dto.setAchievements(education.getAchievements());
        dto.setRelevantCoursework(education.getRelevantCoursework());
        dto.setDisplayOrder(education.getDisplayOrder());
        return dto;
    }
    
    private SocialLinkDTO convertToSocialLinkDTO(SocialLink socialLink) {
        SocialLinkDTO dto = new SocialLinkDTO();
        dto.setId(socialLink.getId());
        dto.setPlatform(socialLink.getPlatform());
        dto.setUrl(socialLink.getUrl());
        dto.setDisplayOrder(socialLink.getDisplayOrder());
        return dto;
    }
    
    private String convertListToJson(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(list);
        } catch (Exception e) {
            return null;
        }
    }
    
    private List<String> convertJsonToList(String json) {
        if (json == null || json.isEmpty()) return new ArrayList<>();
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
