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
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "follows",
       uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"}))
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private Users follower;  // User who is following

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private Users following; // User being followed

    @Column(nullable = false)
    private Date created_on;

    // Default constructor
    public Follow() {
        this.created_on = new Date();
    }

    // Parameterized constructor
    public Follow(Users follower, Users following) {
        this.follower = follower;
        this.following = following;
        this.created_on = new Date();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getFollower() {
        return follower;
    }

    public void setFollower(Users follower) {
        this.follower = follower;
    }

    public Users getFollowing() {
        return following;
    }

    public void setFollowing(Users following) {
        this.following = following;
    }

    public Date getCreated_on() {
        return created_on;
    }

    public void setCreated_on(Date created_on) {
        this.created_on = created_on;
    }

    @Override
    public String toString() {
        return "Follow [id=" + id + ", follower_id=" + (follower != null ? follower.getUser_id() : null) 
                + ", following_id=" + (following != null ? following.getUser_id() : null) 
                + ", created_on=" + created_on + "]";
    }
}
