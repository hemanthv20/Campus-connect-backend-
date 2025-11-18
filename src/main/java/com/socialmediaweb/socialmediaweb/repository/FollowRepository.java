package com.socialmediaweb.socialmediaweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socialmediaweb.socialmediaweb.entities.Follow;
import com.socialmediaweb.socialmediaweb.entities.Users;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    
    // Check if follow relationship exists
    boolean existsByFollowerAndFollowing(Users follower, Users following);
    
    // Find specific follow relationship
    Optional<Follow> findByFollowerAndFollowing(Users follower, Users following);
    
    // Get all followers of a user (users who follow this user)
    List<Follow> findByFollowing(Users following);
    
    // Get all users that a user is following
    List<Follow> findByFollower(Users follower);
    
    // Count followers (how many users follow this user)
    long countByFollowing(Users following);
    
    // Count following (how many users this user is following)
    long countByFollower(Users follower);
    
    // Delete all follows when user is deleted
    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower = :user OR f.following = :user")
    void deleteByFollowerOrFollowing(@Param("user") Users user);
}
