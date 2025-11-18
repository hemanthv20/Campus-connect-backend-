package com.socialmediaweb.socialmediaweb.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialmediaweb.socialmediaweb.entities.Follow;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.FollowRepository;
import com.socialmediaweb.socialmediaweb.repository.UserRepository;

@Service
public class FollowService {
    
    @Autowired
    private FollowRepository followRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    // Follow a user
    @Transactional
    public Follow followUser(int followerId, int followingId) {
        // Validate that users cannot follow themselves
        if (followerId == followingId) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }
        
        // Get users from database
        Users follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower user not found"));
        Users following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Following user not found"));
        
        // Check if already following
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("Already following this user");
        }
        
        // Create and save follow relationship
        Follow follow = new Follow(follower, following);
        follow.setCreated_on(new Date());
        return followRepository.save(follow);
    }
    
    // Unfollow a user
    @Transactional
    public void unfollowUser(int followerId, int followingId) {
        // Get users from database
        Users follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower user not found"));
        Users following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Following user not found"));
        
        // Find and delete the follow relationship
        Optional<Follow> followOpt = followRepository.findByFollowerAndFollowing(follower, following);
        if (followOpt.isPresent()) {
            followRepository.delete(followOpt.get());
        } else {
            throw new IllegalStateException("Follow relationship not found");
        }
    }
    
    // Check if user1 follows user2
    public boolean isFollowing(int followerId, int followingId) {
        Users follower = userRepository.findById(followerId).orElse(null);
        Users following = userRepository.findById(followingId).orElse(null);
        
        if (follower == null || following == null) {
            return false;
        }
        
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }
    
    // Get followers list (users who follow this user)
    public List<Users> getFollowers(int userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<Follow> follows = followRepository.findByFollowing(user);
        List<Users> followers = new ArrayList<>();
        for (Follow follow : follows) {
            followers.add(follow.getFollower());
        }
        return followers;
    }
    
    // Get following list (users that this user is following)
    public List<Users> getFollowing(int userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<Follow> follows = followRepository.findByFollower(user);
        List<Users> following = new ArrayList<>();
        for (Follow follow : follows) {
            following.add(follow.getFollowing());
        }
        return following;
    }
    
    // Get follower count
    public long getFollowerCount(int userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }
        return followRepository.countByFollowing(user);
    }
    
    // Get following count
    public long getFollowingCount(int userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }
        return followRepository.countByFollower(user);
    }
    
    // Check if mutual follow exists (both users follow each other)
    public boolean isMutualFollow(int user1Id, int user2Id) {
        Users user1 = userRepository.findById(user1Id).orElse(null);
        Users user2 = userRepository.findById(user2Id).orElse(null);
        
        if (user1 == null || user2 == null) {
            return false;
        }
        
        boolean user1FollowsUser2 = followRepository.existsByFollowerAndFollowing(user1, user2);
        boolean user2FollowsUser1 = followRepository.existsByFollowerAndFollowing(user2, user1);
        
        return user1FollowsUser2 && user2FollowsUser1;
    }
    
    // Get mutual followers (users who follow you AND you follow back)
    public List<Users> getMutualFollowers(int userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        List<Users> followers = getFollowers(userId);
        List<Users> mutualFollowers = new ArrayList<>();
        
        for (Users follower : followers) {
            if (isFollowing(userId, follower.getUser_id())) {
                mutualFollowers.add(follower);
            }
        }
        
        return mutualFollowers;
    }
}
