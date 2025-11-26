package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    
    @Query("SELECT us FROM UserSkill us WHERE us.user.userId = :userId")
    List<UserSkill> findByUserUserId(@Param("userId") Long userId);
    
    @Query("SELECT us FROM UserSkill us WHERE us.user.userId = :userId AND us.skill.id = :skillId")
    Optional<UserSkill> findByUserUserIdAndSkillId(@Param("userId") Long userId, @Param("skillId") Long skillId);
    
    @Query("SELECT us FROM UserSkill us WHERE us.user.userId = :userId ORDER BY us.displayOrder ASC, us.createdAt DESC")
    List<UserSkill> findByUserIdOrderByDisplayOrder(@Param("userId") Long userId);
    
    @Query("SELECT us FROM UserSkill us WHERE us.user.userId = :userId AND us.isFeatured = true")
    List<UserSkill> findFeaturedSkillsByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM UserSkill us WHERE us.user.userId = :userId AND us.skill.id = :skillId")
    void deleteByUserUserIdAndSkillId(@Param("userId") Long userId, @Param("skillId") Long skillId);
}
