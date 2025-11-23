package com.socialmediaweb.socialmediaweb.controller;

import com.socialmediaweb.socialmediaweb.dto.*;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UsersRepository;
import com.socialmediaweb.socialmediaweb.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    
    @Autowired
    private ProfileService profileService;
    
    @Autowired
    private UsersRepository usersRepository;
    
    // ==================== SKILLS ENDPOINTS ====================
    
    @GetMapping("/{userId}/skills")
    public ResponseEntity<List<UserSkillDTO>> getUserSkills(@PathVariable int userId) {
        try {
            List<UserSkillDTO> skills = profileService.getUserSkills(userId);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{userId}/skills")
    public ResponseEntity<UserSkillDTO> addUserSkill(
            @PathVariable int userId, 
            @RequestBody UserSkillDTO skillDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            UserSkillDTO created = profileService.addUserSkill(userId, skillDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/skills/{userSkillId}")
    public ResponseEntity<UserSkillDTO> updateUserSkill(
            @PathVariable Long userSkillId,
            @RequestBody UserSkillDTO skillDTO) {
        try {
            UserSkillDTO updated = profileService.updateUserSkill(userSkillId, skillDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{userId}/skills/{skillId}")
    public ResponseEntity<Void> deleteUserSkill(
            @PathVariable int userId,
            @PathVariable Long skillId) {
        try {
            profileService.deleteUserSkill(userId, skillId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/skills/search")
    public ResponseEntity<List<SkillDTO>> searchSkills(@RequestParam String query) {
        try {
            List<SkillDTO> skills = profileService.searchSkills(query);
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/skills/all")
    public ResponseEntity<List<SkillDTO>> getAllSkills() {
        try {
            List<SkillDTO> skills = profileService.getAllSkills();
            System.out.println("GET /api/profile/skills/all - Returning " + skills.size() + " skills");
            return ResponseEntity.ok(skills);
        } catch (Exception e) {
            System.err.println("Error in getAllSkills: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== INTERESTS ENDPOINTS ====================
    
    @GetMapping("/{userId}/interests")
    public ResponseEntity<List<UserInterestDTO>> getUserInterests(@PathVariable int userId) {
        try {
            List<UserInterestDTO> interests = profileService.getUserInterests(userId);
            return ResponseEntity.ok(interests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{userId}/interests")
    public ResponseEntity<UserInterestDTO> addUserInterest(
            @PathVariable int userId,
            @RequestBody UserInterestDTO interestDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            UserInterestDTO created = profileService.addUserInterest(userId, interestDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{userId}/interests/{interestId}")
    public ResponseEntity<Void> deleteUserInterest(
            @PathVariable int userId,
            @PathVariable Long interestId) {
        try {
            profileService.deleteUserInterest(userId, interestId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/interests/search")
    public ResponseEntity<List<InterestDTO>> searchInterests(@RequestParam String query) {
        try {
            List<InterestDTO> interests = profileService.searchInterests(query);
            return ResponseEntity.ok(interests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/interests/all")
    public ResponseEntity<List<InterestDTO>> getAllInterests() {
        try {
            List<InterestDTO> interests = profileService.getAllInterests();
            System.out.println("GET /api/profile/interests/all - Returning " + interests.size() + " interests");
            return ResponseEntity.ok(interests);
        } catch (Exception e) {
            System.err.println("Error in getAllInterests: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== GOALS ENDPOINTS ====================
    
    @GetMapping("/{userId}/goals")
    public ResponseEntity<List<GoalDTO>> getUserGoals(@PathVariable int userId) {
        try {
            List<GoalDTO> goals = profileService.getUserGoals(userId);
            return ResponseEntity.ok(goals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/goals/{goalId}")
    public ResponseEntity<GoalDTO> getGoalById(@PathVariable Long goalId) {
        try {
            GoalDTO goal = profileService.getGoalById(goalId);
            return ResponseEntity.ok(goal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PostMapping("/{userId}/goals")
    public ResponseEntity<GoalDTO> createGoal(
            @PathVariable int userId,
            @RequestBody GoalDTO goalDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            GoalDTO created = profileService.createGoal(userId, goalDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/goals/{goalId}")
    public ResponseEntity<GoalDTO> updateGoal(
            @PathVariable Long goalId,
            @RequestBody GoalDTO goalDTO) {
        try {
            GoalDTO updated = profileService.updateGoal(goalId, goalDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/goals/{goalId}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long goalId) {
        try {
            profileService.deleteGoal(goalId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== PROJECTS ENDPOINTS ====================
    
    @GetMapping("/{userId}/projects")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(@PathVariable int userId) {
        try {
            List<ProjectDTO> projects = profileService.getUserProjects(userId);
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{userId}/projects")
    public ResponseEntity<ProjectDTO> createProject(
            @PathVariable int userId,
            @RequestBody ProjectDTO projectDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            ProjectDTO created = profileService.createProject(userId, projectDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable Long projectId,
            @RequestBody ProjectDTO projectDTO) {
        try {
            ProjectDTO updated = profileService.updateProject(projectId, projectDTO);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        try {
            profileService.deleteProject(projectId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== EXPERIENCE ENDPOINTS ====================
    
    @GetMapping("/{userId}/experiences")
    public ResponseEntity<List<ExperienceDTO>> getUserExperiences(@PathVariable int userId) {
        try {
            List<ExperienceDTO> experiences = profileService.getUserExperiences(userId);
            return ResponseEntity.ok(experiences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{userId}/experiences")
    public ResponseEntity<ExperienceDTO> createExperience(
            @PathVariable int userId,
            @RequestBody ExperienceDTO experienceDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            ExperienceDTO created = profileService.createExperience(userId, experienceDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/experiences/{experienceId}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long experienceId) {
        try {
            profileService.deleteExperience(experienceId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== EDUCATION ENDPOINTS ====================
    
    @GetMapping("/{userId}/education")
    public ResponseEntity<List<EducationDTO>> getUserEducation(@PathVariable int userId) {
        try {
            List<EducationDTO> education = profileService.getUserEducation(userId);
            return ResponseEntity.ok(education);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{userId}/education")
    public ResponseEntity<EducationDTO> createEducation(
            @PathVariable int userId,
            @RequestBody EducationDTO educationDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            EducationDTO created = profileService.createEducation(userId, educationDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/education/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId) {
        try {
            profileService.deleteEducation(educationId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ==================== SOCIAL LINKS ENDPOINTS ====================
    
    @GetMapping("/{userId}/social-links")
    public ResponseEntity<List<SocialLinkDTO>> getUserSocialLinks(@PathVariable int userId) {
        try {
            List<SocialLinkDTO> socialLinks = profileService.getUserSocialLinks(userId);
            return ResponseEntity.ok(socialLinks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/{userId}/social-links")
    public ResponseEntity<SocialLinkDTO> addSocialLink(
            @PathVariable int userId,
            @RequestBody SocialLinkDTO socialLinkDTO) {
        try {
            Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
            SocialLinkDTO created = profileService.addSocialLink(userId, socialLinkDTO, user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/social-links/{linkId}")
    public ResponseEntity<Void> deleteSocialLink(@PathVariable Long linkId) {
        try {
            profileService.deleteSocialLink(linkId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
