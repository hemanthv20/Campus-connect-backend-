package com.socialmediaweb.socialmediaweb.dto;

import com.socialmediaweb.socialmediaweb.entities.Chat;
import com.socialmediaweb.socialmediaweb.entities.Message;
import com.socialmediaweb.socialmediaweb.entities.Users;

public class ChatMapper {
    
    // Convert Chat to ChatDTO
    public static ChatDTO toChatDTO(Chat chat, Users currentUser, String lastMessageContent, long unreadCount) {
        // Determine the other user in the chat
        Users otherUser = chat.getUser1().getUser_id() == currentUser.getUser_id() 
                ? chat.getUser2() 
                : chat.getUser1();
        
        UserDTO otherUserDTO = UserMapper.toDTO(otherUser);
        
        // Truncate last message to 50 characters
        String truncatedMessage = lastMessageContent;
        if (truncatedMessage != null && truncatedMessage.length() > 50) {
            truncatedMessage = truncatedMessage.substring(0, 50) + "...";
        }
        
        return new ChatDTO(
            chat.getId(),
            otherUserDTO,
            truncatedMessage,
            chat.getLastMessageAt(),
            unreadCount
        );
    }
    
    // Convert Message to MessageDTO
    public static MessageDTO toMessageDTO(Message message) {
        return new MessageDTO(
            message.getId(),
            message.getChat().getId(),
            message.getSender().getUser_id(),
            message.getSender().getFirst_name() + " " + message.getSender().getLast_name(),
            message.getSender().getProfile_picture(),
            message.getContent(),
            message.getCreatedOn(),
            message.isRead()
        );
    }
}
