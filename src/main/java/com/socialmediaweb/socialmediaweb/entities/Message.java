package com.socialmediaweb.socialmediaweb.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Users receiver;

    @Column(nullable = false, length = 100)
    private String content;

    @Column(nullable = false)
    private Date createdOn;

    @Column(nullable = false)
    private boolean isRead = false;

    // Default constructor
    public Message() {
        this.createdOn = new Date();
        this.isRead = false;
    }

    // Parameterized constructor
    public Message(Chat chat, Users sender, Users receiver, String content) {
        this.chat = chat;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.createdOn = new Date();
        this.isRead = false;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Users getReceiver() {
        return receiver;
    }

    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Message [id=" + id + ", chat_id=" + (chat != null ? chat.getId() : null) 
                + ", sender_id=" + (sender != null ? sender.getUser_id() : null) 
                + ", receiver_id=" + (receiver != null ? receiver.getUser_id() : null) 
                + ", content=" + content + ", createdOn=" + createdOn + ", isRead=" + isRead + "]";
    }
}
