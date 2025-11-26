package com.socialmediaweb.socialmediaweb.config;

import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeeder.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
    }

    private void seedAdminUser() {
        try {
            // Check if admin user already exists
            if (usersRepository.findByUsername("admin").isPresent()) {
                logger.info("Admin user already exists, skipping seeding");
                return;
            }

            // Create admin user
            Users adminUser = new Users();
            adminUser.setUsername("admin");
            adminUser.setPasswordHash(passwordEncoder.encode("Admin@123"));
            adminUser.setFirstName("System");
            adminUser.setLastName("Administrator");
            adminUser.setEmail("admin@campusconnect.com");
            adminUser.setAdmin(true);
            adminUser.setCreatedAt(LocalDateTime.now());
            adminUser.setUpdatedAt(LocalDateTime.now());

            // Save admin user
            Users savedAdmin = usersRepository.save(adminUser);
            
            logger.info("✅ Admin user created successfully with ID: {}", savedAdmin.getUserId());
            logger.info("   Username: admin");
            logger.info("   Password: Admin@123");
            logger.info("   Email: admin@campusconnect.com");
            
        } catch (Exception e) {
            logger.error("❌ Failed to create admin user: {}", e.getMessage(), e);
        }
    }
}