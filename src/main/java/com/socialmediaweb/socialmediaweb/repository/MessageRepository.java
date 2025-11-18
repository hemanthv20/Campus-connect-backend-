package com.socialmediaweb.socialmediaweb.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.socialmediaweb.socialmediaweb.entities.Chat;
import com.socialmediaweb.socialmediaweb.entities.Message;
import com.socialmediaweb.socialmediaweb.entities.Users;

public interface MessageRepository extends JpaRepository<Message, Long> {
    
    // Get messages for a chat with pagination (most recent first)
    Page<Message> findByChatOrderByCreatedOnDesc(Chat chat, Pageable pageable);
    
    // Get unread message count for a user across all chats
    long countByReceiverAndIsReadFalse(Users receiver);
    
    // Get unread message count for a specific chat
    long countByChatAndReceiverAndIsReadFalse(Chat chat, Users receiver);
    
    // Mark all messages in a chat as read for a user
    @Modifying
    @Query("UPDATE Message m SET m.isRead = true WHERE m.chat = :chat AND m.receiver = :receiver AND m.isRead = false")
    void markChatMessagesAsRead(@Param("chat") Chat chat, @Param("receiver") Users receiver);
    
    // Get latest message for a chat
    Optional<Message> findFirstByChatOrderByCreatedOnDesc(Chat chat);
}
