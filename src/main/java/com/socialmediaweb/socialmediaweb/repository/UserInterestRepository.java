package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
    
    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.userId = :userId")
    List<UserInterest> findByUserUserId(@Param("userId") Long userId);
    
    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.interest.id = :interestId")
    Optional<UserInterest> findByUserUserIdAndInterestId(@Param("userId") Long userId, @Param("interestId") Long interestId);
    
    @Query("SELECT ui FROM UserInterest ui WHERE ui.user.userId = :userId ORDER BY ui.displayOrder ASC, ui.createdAt DESC")
    List<UserInterest> findByUserIdOrderByDisplayOrder(@Param("userId") Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM UserInterest ui WHERE ui.user.userId = :userId AND ui.interest.id = :interestId")
    void deleteByUserUserIdAndInterestId(@Param("userId") Long userId, @Param("interestId") Long interestId);
}
