package com.socialmediaweb.socialmediaweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.socialmediaweb.socialmediaweb.dto.ChatDTO;
import com.socialmediaweb.socialmediaweb.dto.ChatMapper;
import com.socialmediaweb.socialmediaweb.dto.MessageDTO;
import com.socialmediaweb.socialmediaweb.entities.Chat;
import com.socialmediaweb.socialmediaweb.entities.Message;
import com.socialmediaweb.socialmediaweb.entities.Users;
import com.socialmediaweb.socialmediaweb.repository.UserRepository;
import com.socialmediaweb.socialmediaweb.service.ChatService;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private com.socialmediaweb.socialmediaweb.service.FollowService followService;
    
    // Get all chats for current user
    @GetMapping
    public ResponseEntity<List<ChatDTO>> getUserChats(@RequestParam int userId) {
        try {
            Users currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            List<Chat> chats = chatService.getUserChats(userId);
            List<ChatDTO> chatDTOs = new ArrayList<>();
            
            for (Chat chat : chats) {
                // Get latest message
                Optional<Message> latestMessage = chatService.getLatestMessage(chat.getId());
                String lastMessageContent = latestMessage.map(Message::getContent).orElse(null);
                
                // Get unread count
                long unreadCount = chatService.getChatUnreadCount(chat.getId(), userId);
                
                ChatDTO chatDTO = ChatMapper.toChatDTO(chat, currentUser, lastMessageContent, unreadCount);
                chatDTOs.add(chatDTO);
            }
            
            return ResponseEntity.ok(chatDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Get or create chat with specific user
    @GetMapping("/with/{otherUserId}")
    public ResponseEntity<?> getOrCreateChat(@RequestParam int userId, @PathVariable int otherUserId) {
        try {
            Chat chat = chatService.getOrCreateChat(userId, otherUserId);
            
            Users currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            
            // Get latest message
            Optional<Message> latestMessage = chatService.getLatestMessage(chat.getId());
            String lastMessageContent = latestMessage.map(Message::getContent).orElse(null);
            
            // Get unread count
            long unreadCount = chatService.getChatUnreadCount(chat.getId(), userId);
            
            ChatDTO chatDTO = ChatMapper.toChatDTO(chat, currentUser, lastMessageContent, unreadCount);
            return ResponseEntity.ok(chatDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    // Get messages for a chat with pagination
    @GetMapping("/{chatId}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            Page<Message> messagesPage = chatService.getMessages(chatId, page, size);
            
            List<MessageDTO> messageDTOs = new ArrayList<>();
            for (Message message : messagesPage.getContent()) {
                messageDTOs.add(ChatMapper.toMessageDTO(message));
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("messages", messageDTOs);
            response.put("currentPage", messagesPage.getNumber());
            response.put("totalPages", messagesPage.getTotalPages());
            response.put("totalMessages", messagesPage.getTotalElements());
            response.put("hasMore", messagesPage.hasNext());
            
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    // Send a message
    @PostMapping("/{chatId}/messages")
    public ResponseEntity<?> sendMessage(
            @PathVariable Long chatId,
            @RequestBody Map<String, Object> request) {
        try {
            int senderId = (Integer) request.get("senderId");
            String content = (String) request.get("content");
            
            Message message = chatService.sendMessage(chatId, senderId, content);
            MessageDTO messageDTO = ChatMapper.toMessageDTO(message);
            
            return ResponseEntity.ok(messageDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    // Mark messages as read
    @PutMapping("/{chatId}/read")
    public ResponseEntity<?> markAsRead(
            @PathVariable Long chatId,
            @RequestParam int userId) {
        try {
            chatService.markMessagesAsRead(chatId, userId);
            return ResponseEntity.ok("Messages marked as read");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    // Delete a message
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Long messageId,
            @RequestParam int userId) {
        try {
            chatService.deleteMessage(messageId, userId);
            return ResponseEntity.ok("Message deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    // Get unread count
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(@RequestParam int userId) {
        try {
            long count = chatService.getUnreadCount(userId);
            Map<String, Long> response = new HashMap<>();
            response.put("count", count);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    // Check if users can chat (one-way follow check)
    @GetMapping("/can-chat")
    public ResponseEntity<Map<String, Boolean>> canChat(
            @RequestParam int user1Id,
            @RequestParam int user2Id) {
        try {
            // User1 can chat with User2 if User1 follows User2
            boolean canChat = followService.isFollowing(user1Id, user2Id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("canChat", canChat);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
