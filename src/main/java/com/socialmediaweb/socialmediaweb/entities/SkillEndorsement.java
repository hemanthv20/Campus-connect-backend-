package com.socialmediaweb.socialmediaweb.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "skill_endorsements", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_skill_id", "endorser_id"})
})
public class SkillEndorsement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_skill_id", nullable = false)
    private UserSkill userSkill;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "endorser_id", nullable = false)
    private Users endorser;
    
    @Column(columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    
    public SkillEndorsement() {}
    
    public SkillEndorsement(UserSkill userSkill, Users endorser) {
        this.userSkill = userSkill;
        this.endorser = endorser;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public UserSkill getUserSkill() { return userSkill; }
    public void setUserSkill(UserSkill userSkill) { this.userSkill = userSkill; }
    
    public Users getEndorser() { return endorser; }
    public void setEndorser(Users endorser) { this.endorser = endorser; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
