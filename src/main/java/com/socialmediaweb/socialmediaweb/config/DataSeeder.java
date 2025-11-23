package com.socialmediaweb.socialmediaweb.config;

import com.socialmediaweb.socialmediaweb.entities.*;
import com.socialmediaweb.socialmediaweb.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataSeeder implements ApplicationRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    
    @Autowired
    private SkillCategoryRepository skillCategoryRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private InterestCategoryRepository interestCategoryRepository;
    
    @Autowired
    private InterestRepository interestRepository;
    
    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        logger.info("=== Starting Data Seeding ===");
        
        try {
            seedSkillCategories();
            seedSkills();
            seedInterestCategories();
            seedInterests();
            
            logger.info("=== Data Seeding Completed Successfully ===");
        } catch (Exception e) {
            logger.error("Error during data seeding: " + e.getMessage(), e);
        }
    }
    
    private void seedSkillCategories() {
        logger.info("Seeding skill categories...");
        
        createSkillCategoryIfNotExists("Programming Languages", "Programming and scripting languages", "code");
        createSkillCategoryIfNotExists("Frameworks & Tools", "Development frameworks and tools", "tools");
        createSkillCategoryIfNotExists("Soft Skills", "Interpersonal and professional skills", "users");
        createSkillCategoryIfNotExists("Design", "Design and creative tools", "palette");
        createSkillCategoryIfNotExists("Data Science", "Data analysis and machine learning", "database");
        createSkillCategoryIfNotExists("Mobile Development", "Mobile app development", "smartphone");
        createSkillCategoryIfNotExists("DevOps", "DevOps and cloud technologies", "cloud");
        
        logger.info("Skill categories seeded: " + skillCategoryRepository.count());
    }
    
    private void seedSkills() {
        logger.info("Seeding skills...");
        
        SkillCategory programming = skillCategoryRepository.findByName("Programming Languages").orElse(null);
        SkillCategory frameworks = skillCategoryRepository.findByName("Frameworks & Tools").orElse(null);
        SkillCategory softSkills = skillCategoryRepository.findByName("Soft Skills").orElse(null);
        SkillCategory design = skillCategoryRepository.findByName("Design").orElse(null);
        SkillCategory dataScience = skillCategoryRepository.findByName("Data Science").orElse(null);
        SkillCategory mobile = skillCategoryRepository.findByName("Mobile Development").orElse(null);
        SkillCategory devops = skillCategoryRepository.findByName("DevOps").orElse(null);
        
        // Programming Languages
        if (programming != null) {
            createSkillIfNotExists("Java", programming, "Object-oriented programming language");
            createSkillIfNotExists("Python", programming, "High-level programming language");
            createSkillIfNotExists("JavaScript", programming, "Dynamic programming language for web");
            createSkillIfNotExists("TypeScript", programming, "Typed superset of JavaScript");
            createSkillIfNotExists("C++", programming, "High-performance programming language");
            createSkillIfNotExists("C#", programming, "Microsoft's object-oriented language");
            createSkillIfNotExists("Go", programming, "Google's systems programming language");
            createSkillIfNotExists("Rust", programming, "Memory-safe systems programming");
            createSkillIfNotExists("PHP", programming, "Server-side scripting language");
            createSkillIfNotExists("Ruby", programming, "Dynamic, object-oriented language");
        }
        
        // Frameworks & Tools
        if (frameworks != null) {
            createSkillIfNotExists("React", frameworks, "JavaScript library for building UIs");
            createSkillIfNotExists("Angular", frameworks, "TypeScript-based web framework");
            createSkillIfNotExists("Vue.js", frameworks, "Progressive JavaScript framework");
            createSkillIfNotExists("Node.js", frameworks, "JavaScript runtime environment");
            createSkillIfNotExists("Spring Boot", frameworks, "Java framework for microservices");
            createSkillIfNotExists("Django", frameworks, "Python web framework");
            createSkillIfNotExists("Flask", frameworks, "Lightweight Python web framework");
            createSkillIfNotExists("Express.js", frameworks, "Node.js web framework");
            createSkillIfNotExists("Next.js", frameworks, "React framework for production");
            createSkillIfNotExists("Laravel", frameworks, "PHP web framework");
        }
        
        // Soft Skills
        if (softSkills != null) {
            createSkillIfNotExists("Communication", softSkills, "Effective verbal and written communication");
            createSkillIfNotExists("Leadership", softSkills, "Team leadership and management");
            createSkillIfNotExists("Teamwork", softSkills, "Collaborative work skills");
            createSkillIfNotExists("Problem Solving", softSkills, "Analytical and critical thinking");
            createSkillIfNotExists("Time Management", softSkills, "Efficient task prioritization");
            createSkillIfNotExists("Critical Thinking", softSkills, "Logical analysis and reasoning");
        }
        
        // Design
        if (design != null) {
            createSkillIfNotExists("UI/UX Design", design, "User interface and experience design");
            createSkillIfNotExists("Figma", design, "Collaborative design tool");
            createSkillIfNotExists("Adobe XD", design, "UI/UX design and prototyping");
            createSkillIfNotExists("Photoshop", design, "Image editing software");
            createSkillIfNotExists("Illustrator", design, "Vector graphics editor");
        }
        
        // Data Science
        if (dataScience != null) {
            createSkillIfNotExists("Machine Learning", dataScience, "AI and predictive modeling");
            createSkillIfNotExists("Data Analysis", dataScience, "Statistical data analysis");
            createSkillIfNotExists("TensorFlow", dataScience, "Machine learning framework");
            createSkillIfNotExists("PyTorch", dataScience, "Deep learning framework");
            createSkillIfNotExists("Pandas", dataScience, "Data manipulation library");
            createSkillIfNotExists("NumPy", dataScience, "Numerical computing library");
        }
        
        // Mobile Development
        if (mobile != null) {
            createSkillIfNotExists("React Native", mobile, "Cross-platform mobile framework");
            createSkillIfNotExists("Flutter", mobile, "Google's UI toolkit for mobile");
            createSkillIfNotExists("Swift", mobile, "iOS app development language");
            createSkillIfNotExists("Kotlin", mobile, "Android app development language");
            createSkillIfNotExists("Android Development", mobile, "Native Android development");
            createSkillIfNotExists("iOS Development", mobile, "Native iOS development");
        }
        
        // DevOps
        if (devops != null) {
            createSkillIfNotExists("Docker", devops, "Containerization platform");
            createSkillIfNotExists("Kubernetes", devops, "Container orchestration");
            createSkillIfNotExists("AWS", devops, "Amazon Web Services cloud platform");
            createSkillIfNotExists("Azure", devops, "Microsoft cloud platform");
            createSkillIfNotExists("CI/CD", devops, "Continuous integration and deployment");
            createSkillIfNotExists("Jenkins", devops, "Automation server");
        }
        
        logger.info("Skills seeded: " + skillRepository.count());
    }
    
    private void seedInterestCategories() {
        logger.info("Seeding interest categories...");
        
        createInterestCategoryIfNotExists("Technology & Innovation", "Tech trends and innovations", "cpu");
        createInterestCategoryIfNotExists("Arts & Culture", "Creative arts and cultural activities", "palette");
        createInterestCategoryIfNotExists("Sports & Fitness", "Physical activities and sports", "activity");
        createInterestCategoryIfNotExists("Business & Entrepreneurship", "Business and startup ventures", "briefcase");
        createInterestCategoryIfNotExists("Social Causes", "Social impact and volunteering", "heart");
        createInterestCategoryIfNotExists("Academic Research", "Research and academic pursuits", "book");
        createInterestCategoryIfNotExists("Hobbies & Entertainment", "Leisure activities and hobbies", "star");
        
        logger.info("Interest categories seeded: " + interestCategoryRepository.count());
    }
    
    private void seedInterests() {
        logger.info("Seeding interests...");
        
        InterestCategory tech = interestCategoryRepository.findByName("Technology & Innovation").orElse(null);
        InterestCategory arts = interestCategoryRepository.findByName("Arts & Culture").orElse(null);
        InterestCategory sports = interestCategoryRepository.findByName("Sports & Fitness").orElse(null);
        InterestCategory business = interestCategoryRepository.findByName("Business & Entrepreneurship").orElse(null);
        InterestCategory social = interestCategoryRepository.findByName("Social Causes").orElse(null);
        InterestCategory academic = interestCategoryRepository.findByName("Academic Research").orElse(null);
        InterestCategory hobbies = interestCategoryRepository.findByName("Hobbies & Entertainment").orElse(null);
        
        // Technology & Innovation
        if (tech != null) {
            createInterestIfNotExists("Artificial Intelligence", tech, "AI and machine learning");
            createInterestIfNotExists("Blockchain", tech, "Distributed ledger technology");
            createInterestIfNotExists("Web Development", tech, "Building web applications");
            createInterestIfNotExists("Mobile Apps", tech, "Mobile application development");
            createInterestIfNotExists("Cybersecurity", tech, "Information security");
            createInterestIfNotExists("Cloud Computing", tech, "Cloud infrastructure and services");
        }
        
        // Arts & Culture
        if (arts != null) {
            createInterestIfNotExists("Music", arts, "Playing and listening to music");
            createInterestIfNotExists("Photography", arts, "Capturing moments through photos");
            createInterestIfNotExists("Painting", arts, "Visual art and painting");
            createInterestIfNotExists("Theater", arts, "Performing arts and drama");
            createInterestIfNotExists("Film Making", arts, "Video production and filmmaking");
            createInterestIfNotExists("Writing", arts, "Creative and technical writing");
        }
        
        // Sports & Fitness
        if (sports != null) {
            createInterestIfNotExists("Football", sports, "Soccer and football");
            createInterestIfNotExists("Basketball", sports, "Basketball sports");
            createInterestIfNotExists("Cricket", sports, "Cricket sports");
            createInterestIfNotExists("Yoga", sports, "Yoga and meditation");
            createInterestIfNotExists("Gym", sports, "Fitness and bodybuilding");
            createInterestIfNotExists("Running", sports, "Running and marathons");
        }
        
        // Business & Entrepreneurship
        if (business != null) {
            createInterestIfNotExists("Startups", business, "Building and growing startups");
            createInterestIfNotExists("Marketing", business, "Digital and traditional marketing");
            createInterestIfNotExists("Finance", business, "Financial management");
            createInterestIfNotExists("Investing", business, "Stock market and investments");
        }
        
        // Social Causes
        if (social != null) {
            createInterestIfNotExists("Environmental Conservation", social, "Protecting the environment");
            createInterestIfNotExists("Education", social, "Educational initiatives");
            createInterestIfNotExists("Healthcare", social, "Health and wellness advocacy");
            createInterestIfNotExists("Community Service", social, "Volunteering and community work");
        }
        
        // Academic Research
        if (academic != null) {
            createInterestIfNotExists("Computer Science", academic, "CS research and theory");
            createInterestIfNotExists("Mathematics", academic, "Mathematical research");
            createInterestIfNotExists("Physics", academic, "Physics and astronomy");
            createInterestIfNotExists("Biology", academic, "Biological sciences");
        }
        
        // Hobbies & Entertainment
        if (hobbies != null) {
            createInterestIfNotExists("Gaming", hobbies, "Video games and esports");
            createInterestIfNotExists("Reading", hobbies, "Books and literature");
            createInterestIfNotExists("Cooking", hobbies, "Culinary arts");
            createInterestIfNotExists("Traveling", hobbies, "Exploring new places");
        }
        
        logger.info("Interests seeded: " + interestRepository.count());
    }
    
    private void createSkillCategoryIfNotExists(String name, String description, String icon) {
        if (!skillCategoryRepository.findByName(name).isPresent()) {
            SkillCategory category = new SkillCategory();
            category.setName(name);
            category.setDescription(description);
            category.setIcon(icon);
            skillCategoryRepository.save(category);
            logger.debug("Created skill category: " + name);
        }
    }
    
    private void createSkillIfNotExists(String name, SkillCategory category, String description) {
        if (!skillRepository.existsByName(name)) {
            Skill skill = new Skill();
            skill.setName(name);
            skill.setCategory(category);
            skill.setDescription(description);
            skill.setIsVerified(true);
            skill.setUsageCount(0);
            skillRepository.save(skill);
            logger.debug("Created skill: " + name);
        }
    }
    
    private void createInterestCategoryIfNotExists(String name, String description, String icon) {
        if (!interestCategoryRepository.findByName(name).isPresent()) {
            InterestCategory category = new InterestCategory();
            category.setName(name);
            category.setDescription(description);
            category.setIcon(icon);
            interestCategoryRepository.save(category);
            logger.debug("Created interest category: " + name);
        }
    }
    
    private void createInterestIfNotExists(String name, InterestCategory category, String description) {
        if (!interestRepository.existsByName(name)) {
            Interest interest = new Interest();
            interest.setName(name);
            interest.setCategory(category);
            interest.setDescription(description);
            interest.setUsageCount(0);
            interestRepository.save(interest);
            logger.debug("Created interest: " + name);
        }
    }
}
