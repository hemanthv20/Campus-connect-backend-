package com.socialmediaweb.socialmediaweb.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UserRepository;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository repository;
    
    // Password encoder for secure password hashing
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Users saveUser(Users user) {
        // Hash password before saving
        if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        return repository.save(user);
    }

    // Save all users
    public List<Users> saveUsers(List<Users> users) {
        return repository.saveAll(users);
    }

    // Get all users
    public List<Users> getUsers() {
        return repository.findAll();
    }

    // Get user by ID
    public Users getUsersById(int id) {
        return repository.findById(id).orElse(null);
    }

    // Delete
    public String deleteUser(int id) {
        repository.deleteById(id);
        return "User deleted.";
    }

    // Authentication with password hashing
    public Users authenticateUser(String username, String password) {
        Users user = repository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    // Updated updateUser method to include college fields
    public Users updateUser(Users user) {
        Users existingUser = repository.findById(user.getUserId()).orElse(null);

        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());
            
            // Only update password if it's provided and different
            if (user.getPasswordHash() != null && !user.getPasswordHash().isEmpty()) {
                // Check if password is already hashed (starts with $2a$ for BCrypt)
                if (!user.getPasswordHash().startsWith("$2a$")) {
                    existingUser.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
                } else {
                    existingUser.setPasswordHash(user.getPasswordHash());
                }
            }
            
            // Note: Gender field doesn't exist in Users entity, removing this line
            existingUser.setProfilePicture(user.getProfilePicture());
            existingUser.setAdmin(user.getAdmin());

            // UPDATE COLLEGE ASSOCIATION FIELDS
            existingUser.setCollege(user.getCollege());
            existingUser.setSemester(user.getSemester());
            existingUser.setBatch(user.getBatch());
        }

        return repository.save(existingUser);
    }

    public boolean isUsernameExists(String username) {
        return repository.existsByUsername(username);
    }

    public boolean isEmailExists(String email) {
        return repository.existsByEmail(email);
    }

    public Users findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public List<String> findUsernamesBySearchTerm(String searchTerm) {
        return repository.findByUsernameContainingIgnoreCase(searchTerm);
    }
}