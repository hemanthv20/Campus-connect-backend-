package com.socialmediaweb.socialmediaweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialmediaweb.socialmediaweb.dto.FollowCountsDTO;
import com.socialmediaweb.socialmediaweb.dto.FollowResponse;
import com.socialmediaweb.socialmediaweb.dto.UserDTO;
import com.socialmediaweb.socialmediaweb.dto.UserMapper;
import com.socialmediaweb.socialmediaweb.entities.Follow;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.service.FollowService;

@RestController
@RequestMapping("/api/follow")
public class FollowController {
    
    @Autowired
    private FollowService followService;
    
    // Follow a user
    @PostMapping
    public ResponseEntity<?> followUser(@RequestBody Map<String, Integer> request) {
        try {
            int followerId = request.get("followerId");
            int followingId = request.get("followingId");
            
            Follow follow = followService.followUser(followerId, followingId);
            
            // Get updated counts
            long followerCount = followService.getFollowerCount(followingId);
            long followingCount = followService.getFollowingCount(followerId);
            
            FollowResponse response = new FollowResponse(
                true, 
                "Successfully followed user", 
                followerCount, 
                followingCount
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new FollowResponse(false, e.getMessage(), 0, 0));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new FollowResponse(false, e.getMessage(), 0, 0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FollowResponse(false, "Internal server error", 0, 0));
        }
    }
    
    // Unfollow a user
    @DeleteMapping
    public ResponseEntity<?> unfollowUser(@RequestBody Map<String, Integer> request) {
        try {
            int followerId = request.get("followerId");
            int followingId = request.get("followingId");
            
            followService.unfollowUser(followerId, followingId);
            
            // Get updated counts
            long followerCount = followService.getFollowerCount(followingId);
            long followingCount = followService.getFollowingCount(followerId);
            
            FollowResponse response = new FollowResponse(
                true, 
                "Successfully unfollowed user", 
                followerCount, 
                followingCount
            );
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FollowResponse(false, e.getMessage(), 0, 0));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new FollowResponse(false, e.getMessage(), 0, 0));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new FollowResponse(false, "Internal server error", 0, 0));
        }
    }
    
    // Check if following
    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkFollowing(
            @RequestParam int followerId, 
            @RequestParam int followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isFollowing", isFollowing);
        return ResponseEntity.ok(response);
    }
    
    // Get followers list
    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable int userId) {
        try {
            List<Users> followers = followService.getFollowers(userId);
            List<UserDTO> followerDTOs = new ArrayList<>();
            
            for (Users user : followers) {
                UserDTO dto = UserMapper.toDTO(user);
                followerDTOs.add(dto);
            }
            
            return ResponseEntity.ok(followerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Get following list
    @GetMapping("/following/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowing(@PathVariable int userId) {
        try {
            List<Users> following = followService.getFollowing(userId);
            List<UserDTO> followingDTOs = new ArrayList<>();
            
            for (Users user : following) {
                UserDTO dto = UserMapper.toDTO(user);
                // Add follow-back status
                dto.setFollower(followService.isFollowing(user.getUserId(), userId));
                dto.setFollowing(true); // Current user follows this user
                dto.setMutualFollow(dto.isFollower() && dto.isFollowing());
                followingDTOs.add(dto);
            }
            
            return ResponseEntity.ok(followingDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    // Get follower and following counts
    @GetMapping("/counts/{userId}")
    public ResponseEntity<FollowCountsDTO> getCounts(@PathVariable int userId) {
        long followerCount = followService.getFollowerCount(userId);
        long followingCount = followService.getFollowingCount(userId);
        
        FollowCountsDTO counts = new FollowCountsDTO(followerCount, followingCount);
        return ResponseEntity.ok(counts);
    }
    
    // Check mutual follow
    @GetMapping("/mutual")
    public ResponseEntity<Map<String, Boolean>> checkMutualFollow(
            @RequestParam int user1Id, 
            @RequestParam int user2Id) {
        boolean isMutual = followService.isMutualFollow(user1Id, user2Id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("isMutual", isMutual);
        return ResponseEntity.ok(response);
    }
    
    // Get mutual followers (users who follow you AND you follow back)
    @GetMapping("/mutual-followers/{userId}")
    public ResponseEntity<List<UserDTO>> getMutualFollowers(@PathVariable int userId) {
        try {
            List<Users> mutualFollowers = followService.getMutualFollowers(userId);
            List<UserDTO> mutualFollowerDTOs = new ArrayList<>();
            
            for (Users user : mutualFollowers) {
                UserDTO dto = UserMapper.toDTO(user);
                mutualFollowerDTOs.add(dto);
            }
            
            return ResponseEntity.ok(mutualFollowerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
