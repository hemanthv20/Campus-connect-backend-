package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.SocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialLinkRepository extends JpaRepository<SocialLink, Long> {
    
    @Query("SELECT sl FROM SocialLink sl WHERE sl.user.user_id = :userId")
    List<SocialLink> findByUserUserId(@Param("userId") int userId);
    
    @Query("SELECT sl FROM SocialLink sl WHERE sl.user.user_id = :userId ORDER BY sl.displayOrder ASC")
    List<SocialLink> findByUserIdOrderByDisplayOrder(@Param("userId") int userId);
}
