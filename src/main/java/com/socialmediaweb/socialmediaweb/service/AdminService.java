package com.socialmediaweb.socialmediaweb.service;

import com.socialmediaweb.socialmediaweb.dto.UserDTO;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Check if user has admin privileges
     */
    public boolean isAdmin(Long userId) {
        try {
            Optional<Users> user = usersRepository.findById(userId);
            return user.isPresent() && Boolean.TRUE.equals(user.get().getAdmin());
        } catch (Exception e) {
            logger.error("Error checking admin status for user {}: {}", userId, e.getMessage());
            return false;
        }
    }

    /**
     * Get all users in the system
     */
    public List<UserDTO> getAllUsers() {
        try {
            List<Users> users = usersRepository.findAll();
            return users.stream()
                    .map(this::convertToUserDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error retrieving all users: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    /**
     * Delete a user and all associated data
     */
    public boolean deleteUser(Long userId) {
        try {
            Optional<Users> user = usersRepository.findById(userId);
            if (user.isPresent()) {
                // Prevent deletion of admin user
                if (Boolean.TRUE.equals(user.get().getAdmin())) {
                    logger.warn("Attempted to delete admin user with ID: {}", userId);
                    return false;
                }

                // Delete user (cascade will handle related data)
                usersRepository.deleteById(userId);
                logger.info("User deleted successfully: {}", userId);
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /**
     * Delete a post
     */
    public boolean deletePost(Long postId) {
        try {
            String checkSql = "SELECT COUNT(*) FROM posts WHERE post_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, postId);
            
            if (count != null && count > 0) {
                String deleteSql = "DELETE FROM posts WHERE post_id = ?";
                int rowsAffected = jdbcTemplate.update(deleteSql, postId);
                
                if (rowsAffected > 0) {
                    logger.info("Post deleted successfully: {}", postId);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Error deleting post {}: {}", postId, e.getMessage());
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    /**
     * Get platform statistics
     */
    public Map<String, Object> getPlatformStatistics() {
        try {
            Map<String, Object> stats = new HashMap<>();
            
            // User statistics
            String userCountSql = "SELECT COUNT(*) FROM users";
            Integer totalUsers = jdbcTemplate.queryForObject(userCountSql, Integer.class);
            stats.put("totalUsers", totalUsers != null ? totalUsers : 0);
            
            String adminCountSql = "SELECT COUNT(*) FROM users WHERE admin = true";
            Integer totalAdmins = jdbcTemplate.queryForObject(adminCountSql, Integer.class);
            stats.put("totalAdmins", totalAdmins != null ? totalAdmins : 0);
            
            // Post statistics
            String postCountSql = "SELECT COUNT(*) FROM posts";
            Integer totalPosts = jdbcTemplate.queryForObject(postCountSql, Integer.class);
            stats.put("totalPosts", totalPosts != null ? totalPosts : 0);
            
            // Follow statistics
            String followCountSql = "SELECT COUNT(*) FROM follows";
            Integer totalFollows = jdbcTemplate.queryForObject(followCountSql, Integer.class);
            stats.put("totalFollows", totalFollows != null ? totalFollows : 0);
            
            // Chat statistics
            String chatCountSql = "SELECT COUNT(*) FROM chats";
            Integer totalChats = jdbcTemplate.queryForObject(chatCountSql, Integer.class);
            stats.put("totalChats", totalChats != null ? totalChats : 0);
            
            String messageCountSql = "SELECT COUNT(*) FROM messages";
            Integer totalMessages = jdbcTemplate.queryForObject(messageCountSql, Integer.class);
            stats.put("totalMessages", totalMessages != null ? totalMessages : 0);
            
            return stats;
        } catch (Exception e) {
            logger.error("Error retrieving platform statistics: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve statistics", e);
        }
    }

    /**
     * Get all posts with user information
     */
    public List<Map<String, Object>> getAllPosts() {
        try {
            String sql = """
                SELECT p.post_id, p.content, p.image, p.video, p.created_on, p.updated_on,
                       u.user_id, u.username, u.first_name, u.last_name, u.profile_picture
                FROM posts p
                JOIN users u ON p.user_id = u.user_id
                ORDER BY p.created_on DESC
                """;
            
            return jdbcTemplate.queryForList(sql);
        } catch (Exception e) {
            logger.error("Error retrieving all posts: {}", e.getMessage());
            throw new RuntimeException("Failed to retrieve posts", e);
        }
    }

    /**
     * Convert Users entity to UserDTO
     */
    private UserDTO convertToUserDTO(Users user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setProfilePicture(user.getProfilePicture());
        dto.setCollege(user.getCollege());
        dto.setSemester(user.getSemester());
        dto.setBatch(user.getBatch());
        dto.setAdmin(user.getAdmin());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}