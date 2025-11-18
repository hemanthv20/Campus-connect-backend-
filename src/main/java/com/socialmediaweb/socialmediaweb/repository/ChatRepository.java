package com.socialmediaweb.socialmediaweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socialmediaweb.socialmediaweb.entities.Chat;
import com.socialmediaweb.socialmediaweb.entities.Users;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    
    // Find chat between two users (order-independent)
    @Query("SELECT c FROM Chat c WHERE (c.user1 = :user1 AND c.user2 = :user2) OR (c.user1 = :user2 AND c.user2 = :user1)")
    Optional<Chat> findByUsers(@Param("user1") Users user1, @Param("user2") Users user2);
    
    // Get all chats for a user, sorted by most recent message
    @Query("SELECT c FROM Chat c WHERE c.user1 = :user OR c.user2 = :user ORDER BY c.lastMessageAt DESC NULLS LAST, c.createdOn DESC")
    List<Chat> findByUser(@Param("user") Users user);
    
    // Delete all chats involving a user
    @Modifying
    @Query("DELETE FROM Chat c WHERE c.user1 = :user OR c.user2 = :user")
    void deleteByUser(@Param("user") Users user);
}
