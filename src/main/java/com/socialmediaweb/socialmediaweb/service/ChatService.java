package com.socialmediaweb.socialmediaweb.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.socialmediaweb.socialmediaweb.entities.Chat;
import com.socialmediaweb.socialmediaweb.entities.Message;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.ChatRepository;
import com.socialmediaweb.socialmediaweb.repository.MessageRepository;
import com.socialmediaweb.socialmediaweb.repository.UserRepository;

@Service
public class ChatService {
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private com.socialmediaweb.socialmediaweb.service.FollowService followService;
    
    // Get or create chat between two users
    @Transactional
    public Chat getOrCreateChat(int user1Id, int user2Id) {
        // Validate users are different
        if (user1Id == user2Id) {
            throw new IllegalArgumentException("Cannot create chat with yourself");
        }
        
        // Check if user1 follows user2 (one-way follow is sufficient)
        boolean user1FollowsUser2 = followService.isFollowing(user1Id, user2Id);
        if (!user1FollowsUser2) {
            throw new IllegalArgumentException("You must follow this user to message them");
        }
        
        // Get users
        Users user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new IllegalArgumentException("User1 not found"));
        Users user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new IllegalArgumentException("User2 not found"));
        
        // Check if chat already exists
        Optional<Chat> existingChat = chatRepository.findByUsers(user1, user2);
        if (existingChat.isPresent()) {
            return existingChat.get();
        }
        
        // Create new chat
        Chat chat = new Chat(user1, user2);
        return chatRepository.save(chat);
    }
    
    // Get all chats for a user
    public java.util.List<Chat> getUserChats(int userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return chatRepository.findByUser(user);
    }
    
    // Send a message
    @Transactional
    public Message sendMessage(Long chatId, int senderId, String content) {
        // Validate content
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        if (content.length() > 100) {
            throw new IllegalArgumentException("Message content cannot exceed 100 characters");
        }
        
        // Get chat
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        
        // Get sender
        Users sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        
        // Determine receiver
        Users receiver;
        if (chat.getUser1().getUser_id() == senderId) {
            receiver = chat.getUser2();
        } else if (chat.getUser2().getUser_id() == senderId) {
            receiver = chat.getUser1();
        } else {
            throw new IllegalArgumentException("Sender is not a participant in this chat");
        }
        
        // Create message
        Message message = new Message(chat, sender, receiver, content.trim());
        Message savedMessage = messageRepository.save(message);
        
        // Update chat's last message timestamp
        chat.setLastMessageAt(new Date());
        chatRepository.save(chat);
        
        return savedMessage;
    }
    
    // Get messages for a chat with pagination
    public Page<Message> getMessages(Long chatId, int page, int size) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return messageRepository.findByChatOrderByCreatedOnDesc(chat, pageable);
    }
    
    // Mark messages as read
    @Transactional
    public void markMessagesAsRead(Long chatId, int userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new IllegalArgumentException("Chat not found"));
        
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        messageRepository.markChatMessagesAsRead(chat, user);
    }
    
    // Delete a message
    @Transactional
    public void deleteMessage(Long messageId, int userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        
        // Only sender can delete their message
        if (message.getSender().getUser_id() != userId) {
            throw new IllegalArgumentException("You can only delete your own messages");
        }
        
        messageRepository.delete(message);
    }
    
    // Get unread count for a user
    public long getUnreadCount(int userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return 0;
        }
        return messageRepository.countByReceiverAndIsReadFalse(user);
    }
    
    // Get unread count for a specific chat
    public long getChatUnreadCount(Long chatId, int userId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        Users user = userRepository.findById(userId).orElse(null);
        
        if (chat == null || user == null) {
            return 0;
        }
        
        return messageRepository.countByChatAndReceiverAndIsReadFalse(chat, user);
    }
    
    // Get latest message for a chat
    public Optional<Message> getLatestMessage(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return Optional.empty();
        }
        return messageRepository.findFirstByChatOrderByCreatedOnDesc(chat);
    }
}
