package com.socialmediaweb.socialmediaweb.controller;

import com.socialmediaweb.socialmediaweb.dto.UserRegistrationDTO;
import com.socialmediaweb.socialmediaweb.dto.UserResponseDTO;
import com.socialmediaweb.socialmediaweb.dto.UsernameCheckDTO;
import com.socialmediaweb.socialmediaweb.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserResponseDTO user = authService.registerUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed. Please try again."));
        }
    }

    /**
     * Check if username is available
     */
    @PostMapping("/check-username")
    public ResponseEntity<UsernameCheckDTO> checkUsername(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new UsernameCheckDTO(false, "Username is required")
                );
            }

            boolean isAvailable = authService.isUsernameAvailable(username);
            String message = isAvailable ? 
                "Username is available" : 
                String.format("Username \"%s\" is already taken", username);
            
            return ResponseEntity.ok(new UsernameCheckDTO(isAvailable, message));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UsernameCheckDTO(false, "Unable to check username availability"));
        }
    }

    /**
     * Validate registration data
     */
    @PostMapping("/validate")
    public ResponseEntity<?> validateRegistration(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            String validationError = authService.validateRegistrationData(registrationDTO);
            if (validationError != null) {
                return ResponseEntity.badRequest().body(Map.of("error", validationError));
            }
            return ResponseEntity.ok(Map.of("message", "Validation passed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Validation failed"));
        }
    }
}