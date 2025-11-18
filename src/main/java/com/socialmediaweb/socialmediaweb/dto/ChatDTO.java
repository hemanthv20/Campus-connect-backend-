package com.socialmediaweb.socialmediaweb.dto;

import java.util.Date;

public class ChatDTO {
    private Long id;
    private UserDTO otherUser;
    private String lastMessageContent;
    private Date lastMessageAt;
    private long unreadCount;
    
    public ChatDTO() {
    }
    
    public ChatDTO(Long id, UserDTO otherUser, String lastMessageContent, Date lastMessageAt, long unreadCount) {
        this.id = id;
        this.otherUser = otherUser;
        this.lastMessageContent = lastMessageContent;
        this.lastMessageAt = lastMessageAt;
        this.unreadCount = unreadCount;
    }
    
    // Getters and setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserDTO getOtherUser() {
        return otherUser;
    }
    
    public void setOtherUser(UserDTO otherUser) {
        this.otherUser = otherUser;
    }
    
    public String getLastMessageContent() {
        return lastMessageContent;
    }
    
    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }
    
    public Date getLastMessageAt() {
        return lastMessageAt;
    }
    
    public void setLastMessageAt(Date lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
    
    public long getUnreadCount() {
        return unreadCount;
    }
    
    public void setUnreadCount(long unreadCount) {
        this.unreadCount = unreadCount;
    }
}
