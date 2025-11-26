package com.socialmediaweb.socialmediaweb.controller;

import com.socialmediaweb.socialmediaweb.dto.UserDTO;
import com.socialmediaweb.socialmediaweb.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * Get all users (Admin only)
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@RequestParam Long adminUserId) {
        try {
            if (!adminService.isAdmin(adminUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied. Admin privileges required."));
            }

            List<UserDTO> users = adminService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve users"));
        }
    }

    /**
     * Delete any user (Admin only)
     */
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId, @RequestParam Long adminUserId) {
        try {
            if (!adminService.isAdmin(adminUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied. Admin privileges required."));
            }

            // Prevent admin from deleting themselves
            if (userId.equals(adminUserId)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Cannot delete your own admin account"));
            }

            boolean deleted = adminService.deleteUser(userId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete user"));
        }
    }

    /**
     * Delete any post (Admin only)
     */
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, @RequestParam Long adminUserId) {
        try {
            if (!adminService.isAdmin(adminUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied. Admin privileges required."));
            }

            boolean deleted = adminService.deletePost(postId);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete post"));
        }
    }

    /**
     * Get platform statistics (Admin only)
     */
    @GetMapping("/stats")
    public ResponseEntity<?> getPlatformStats(@RequestParam Long adminUserId) {
        try {
            if (!adminService.isAdmin(adminUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied. Admin privileges required."));
            }

            Map<String, Object> stats = adminService.getPlatformStatistics();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve statistics"));
        }
    }

    /**
     * Check if user is admin
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<?> checkAdminStatus(@PathVariable Long userId) {
        try {
            boolean isAdmin = adminService.isAdmin(userId);
            return ResponseEntity.ok(Map.of("isAdmin", isAdmin));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to check admin status"));
        }
    }

    /**
     * Get all posts (Admin only)
     */
    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts(@RequestParam Long adminUserId) {
        try {
            if (!adminService.isAdmin(adminUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "Access denied. Admin privileges required."));
            }

            List<Map<String, Object>> posts = adminService.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve posts"));
        }
    }
}