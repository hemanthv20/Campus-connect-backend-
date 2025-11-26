package com.socialmediaweb.socialmediaweb.service;

import com.socialmediaweb.socialmediaweb.dto.UserRegistrationDTO;
import com.socialmediaweb.socialmediaweb.dto.UserResponseDTO;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Validation patterns
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PASSWORD_UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern PASSWORD_NUMBER = Pattern.compile(".*\\d.*");
    private static final Pattern PASSWORD_SPECIAL = Pattern.compile(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");

    /**
     * Register a new user with validation
     */
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        // Validate registration data with priority-based error handling
        String validationError = validateRegistrationData(registrationDTO);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }

        // Create new user entity
        Users user = new Users();
        user.setUsername(registrationDTO.getUsername().trim());
        user.setEmail(registrationDTO.getEmail().trim());
        user.setPasswordHash(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Save user to database
        Users savedUser = usersRepository.save(user);

        // Return user response DTO
        return new UserResponseDTO(
            savedUser.getUserId(),
            savedUser.getUsername(),
            savedUser.getEmail(),
            savedUser.getCreatedAt()
        );
    }

    /**
     * Check if username is available
     */
    public boolean isUsernameAvailable(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return !usersRepository.existsByUsernameIgnoreCase(username.trim());
    }

    /**
     * Validate registration data with priority-based error handling
     * Returns the most critical error first
     */
    public String validateRegistrationData(UserRegistrationDTO registrationDTO) {
        String username = registrationDTO.getUsername();
        String password = registrationDTO.getPassword();
        String email = registrationDTO.getEmail();

        // PRIORITY 1: Username validation and availability
        String usernameError = validateUsername(username);
        if (usernameError != null) {
            return usernameError;
        }

        // Check username availability (highest priority)
        if (!isUsernameAvailable(username)) {
            return String.format("Username \"%s\" is already taken. Please choose a different username.", username);
        }

        // PRIORITY 2: Password length validation
        String passwordLengthError = validatePasswordLength(password);
        if (passwordLengthError != null) {
            return passwordLengthError;
        }

        // PRIORITY 3: Password complexity validation
        String passwordComplexityError = validatePasswordComplexity(password);
        if (passwordComplexityError != null) {
            return passwordComplexityError;
        }

        // PRIORITY 4: Email validation (if provided)
        if (email != null && !email.trim().isEmpty()) {
            String emailError = validateEmail(email);
            if (emailError != null) {
                return emailError;
            }
        }

        // All validations passed
        return null;
    }

    /**
     * Validate username format and requirements
     */
    private String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required.";
        }

        username = username.trim();

        if (username.length() < 3) {
            return "Username must be at least 3 characters long.";
        }

        if (username.length() > 20) {
            return "Username cannot exceed 20 characters.";
        }

        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return "Username can only contain letters, numbers, and underscores.";
        }

        return null;
    }

    /**
     * Validate password length requirements
     */
    private String validatePasswordLength(String password) {
        if (password == null || password.trim().isEmpty()) {
            return "Password is required.";
        }

        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }

        return null;
    }

    /**
     * Validate password complexity requirements
     */
    private String validatePasswordComplexity(String password) {
        if (!PASSWORD_UPPERCASE.matcher(password).matches()) {
            return "Password must contain at least one uppercase letter (A-Z).";
        }

        if (!PASSWORD_NUMBER.matcher(password).matches()) {
            return "Password must contain at least one number (0-9).";
        }

        if (!PASSWORD_SPECIAL.matcher(password).matches()) {
            return "Password must contain at least one special character (!@#$%^&*()_+-=[]{};\\':\"|,.<>/?).";
        }

        return null;
    }

    /**
     * Validate email format
     */
    private String validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            return "Please enter a valid email address (e.g., user@example.com).";
        }

        return null;
    }
}