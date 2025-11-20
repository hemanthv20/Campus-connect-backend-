package com.socialmediaweb.socialmediaweb.controller;

import com.socialmediaweb.socialmediaweb.entities.*;
import com.socialmediaweb.socialmediaweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/seed")
public class DataSeedController {
    
    @Autowired
    private SkillCategoryRepository skillCategoryRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private InterestCategoryRepository interestCategoryRepository;
    
    @Autowired
    private InterestRepository interestRepository;
    
    @PostMapping("/skills")
    public ResponseEntity<Map<String, Object>> seedSkills() {
        Map<String, Object> response = new HashMap<>();
        int count = 0;
        
        System.out.println("=== Starting skill seeding ===");
        
        try {
            // Get categories
            SkillCategory programming = skillCategoryRepository.findByName("Programming Languages").orElse(null);
            SkillCategory frameworks = skillCategoryRepository.findByName("Frameworks & Tools").orElse(null);
            SkillCategory softSkills = skillCategoryRepository.findByName("Soft Skills").orElse(null);
            SkillCategory design = skillCategoryRepository.findByName("Design").orElse(null);
            SkillCategory dataScience = skillCategoryRepository.findByName("Data Science").orElse(null);
            SkillCategory mobile = skillCategoryRepository.findByName("Mobile Development").orElse(null);
            SkillCategory devops = skillCategoryRepository.findByName("DevOps").orElse(null);
            
            // Programming Languages
            if (programming != null) {
                count += createSkillIfNotExists("Java", programming);
                count += createSkillIfNotExists("Python", programming);
                count += createSkillIfNotExists("JavaScript", programming);
                count += createSkillIfNotExists("TypeScript", programming);
                count += createSkillIfNotExists("C++", programming);
                count += createSkillIfNotExists("C#", programming);
                count += createSkillIfNotExists("Go", programming);
                count += createSkillIfNotExists("Rust", programming);
                count += createSkillIfNotExists("PHP", programming);
                count += createSkillIfNotExists("Ruby", programming);
            }
            
            // Frameworks & Tools
            if (frameworks != null) {
                count += createSkillIfNotExists("React", frameworks);
                count += createSkillIfNotExists("Angular", frameworks);
                count += createSkillIfNotExists("Vue.js", frameworks);
                count += createSkillIfNotExists("Node.js", frameworks);
                count += createSkillIfNotExists("Spring Boot", frameworks);
                count += createSkillIfNotExists("Django", frameworks);
                count += createSkillIfNotExists("Flask", frameworks);
                count += createSkillIfNotExists("Express.js", frameworks);
                count += createSkillIfNotExists("Next.js", frameworks);
                count += createSkillIfNotExists("Laravel", frameworks);
            }
            
            // Soft Skills
            if (softSkills != null) {
                count += createSkillIfNotExists("Communication", softSkills);
                count += createSkillIfNotExists("Leadership", softSkills);
                count += createSkillIfNotExists("Teamwork", softSkills);
                count += createSkillIfNotExists("Problem Solving", softSkills);
                count += createSkillIfNotExists("Time Management", softSkills);
                count += createSkillIfNotExists("Critical Thinking", softSkills);
            }
            
            // Design
            if (design != null) {
                count += createSkillIfNotExists("UI/UX Design", design);
                count += createSkillIfNotExists("Figma", design);
                count += createSkillIfNotExists("Adobe XD", design);
                count += createSkillIfNotExists("Photoshop", design);
                count += createSkillIfNotExists("Illustrator", design);
            }
            
            // Data Science
            if (dataScience != null) {
                count += createSkillIfNotExists("Machine Learning", dataScience);
                count += createSkillIfNotExists("Data Analysis", dataScience);
                count += createSkillIfNotExists("TensorFlow", dataScience);
                count += createSkillIfNotExists("PyTorch", dataScience);
                count += createSkillIfNotExists("Pandas", dataScience);
                count += createSkillIfNotExists("NumPy", dataScience);
            }
            
            // Mobile Development
            if (mobile != null) {
                count += createSkillIfNotExists("React Native", mobile);
                count += createSkillIfNotExists("Flutter", mobile);
                count += createSkillIfNotExists("Swift", mobile);
                count += createSkillIfNotExists("Kotlin", mobile);
                count += createSkillIfNotExists("Android Development", mobile);
                count += createSkillIfNotExists("iOS Development", mobile);
            }
            
            // DevOps
            if (devops != null) {
                count += createSkillIfNotExists("Docker", devops);
                count += createSkillIfNotExists("Kubernetes", devops);
                count += createSkillIfNotExists("AWS", devops);
                count += createSkillIfNotExists("Azure", devops);
                count += createSkillIfNotExists("CI/CD", devops);
                count += createSkillIfNotExists("Jenkins", devops);
            }
            
            response.put("success", true);
            response.put("message", "Skills seeded successfully");
            response.put("count", count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    @PostMapping("/interests")
    public ResponseEntity<Map<String, Object>> seedInterests() {
        Map<String, Object> response = new HashMap<>();
        int count = 0;
        
        try {
            // Get categories
            InterestCategory tech = interestCategoryRepository.findByName("Technology & Innovation").orElse(null);
            InterestCategory arts = interestCategoryRepository.findByName("Arts & Culture").orElse(null);
            InterestCategory sports = interestCategoryRepository.findByName("Sports & Fitness").orElse(null);
            InterestCategory business = interestCategoryRepository.findByName("Business & Entrepreneurship").orElse(null);
            InterestCategory social = interestCategoryRepository.findByName("Social Causes").orElse(null);
            InterestCategory academic = interestCategoryRepository.findByName("Academic Research").orElse(null);
            InterestCategory hobbies = interestCategoryRepository.findByName("Hobbies & Entertainment").orElse(null);
            
            // Technology & Innovation
            if (tech != null) {
                count += createInterestIfNotExists("Artificial Intelligence", tech);
                count += createInterestIfNotExists("Blockchain", tech);
                count += createInterestIfNotExists("Web Development", tech);
                count += createInterestIfNotExists("Mobile Apps", tech);
                count += createInterestIfNotExists("Cybersecurity", tech);
            }
            
            // Arts & Culture
            if (arts != null) {
                count += createInterestIfNotExists("Music", arts);
                count += createInterestIfNotExists("Photography", arts);
                count += createInterestIfNotExists("Painting", arts);
                count += createInterestIfNotExists("Theater", arts);
                count += createInterestIfNotExists("Film Making", arts);
            }
            
            // Sports & Fitness
            if (sports != null) {
                count += createInterestIfNotExists("Football", sports);
                count += createInterestIfNotExists("Basketball", sports);
                count += createInterestIfNotExists("Cricket", sports);
                count += createInterestIfNotExists("Yoga", sports);
                count += createInterestIfNotExists("Gym", sports);
                count += createInterestIfNotExists("Running", sports);
            }
            
            // Business & Entrepreneurship
            if (business != null) {
                count += createInterestIfNotExists("Startups", business);
                count += createInterestIfNotExists("Marketing", business);
                count += createInterestIfNotExists("Finance", business);
                count += createInterestIfNotExists("Investing", business);
            }
            
            // Social Causes
            if (social != null) {
                count += createInterestIfNotExists("Environmental Conservation", social);
                count += createInterestIfNotExists("Education", social);
                count += createInterestIfNotExists("Healthcare", social);
                count += createInterestIfNotExists("Community Service", social);
            }
            
            // Academic Research
            if (academic != null) {
                count += createInterestIfNotExists("Computer Science", academic);
                count += createInterestIfNotExists("Mathematics", academic);
                count += createInterestIfNotExists("Physics", academic);
                count += createInterestIfNotExists("Biology", academic);
            }
            
            // Hobbies & Entertainment
            if (hobbies != null) {
                count += createInterestIfNotExists("Gaming", hobbies);
                count += createInterestIfNotExists("Reading", hobbies);
                count += createInterestIfNotExists("Cooking", hobbies);
                count += createInterestIfNotExists("Traveling", hobbies);
            }
            
            response.put("success", true);
            response.put("message", "Interests seeded successfully");
            response.put("count", count);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    private int createSkillIfNotExists(String name, SkillCategory category) {
        if (!skillRepository.existsByName(name)) {
            Skill skill = new Skill();
            skill.setName(name);
            skill.setCategory(category);
            skill.setIsVerified(true);
            skillRepository.save(skill);
            return 1;
        }
        return 0;
    }
    
    private int createInterestIfNotExists(String name, InterestCategory category) {
        if (!interestRepository.existsByName(name)) {
            Interest interest = new Interest();
            interest.setName(name);
            interest.setCategory(category);
            interestRepository.save(interest);
            return 1;
        }
        return 0;
    }
}
