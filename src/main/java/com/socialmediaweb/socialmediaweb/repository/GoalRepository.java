package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    
    @Query("SELECT g FROM Goal g WHERE g.user.user_id = :userId")
    List<Goal> findByUserUserId(@Param("userId") int userId);
    
    @Query("SELECT g FROM Goal g WHERE g.user.user_id = :userId AND g.isPublic = true ORDER BY g.createdAt DESC")
    List<Goal> findPublicGoalsByUserId(@Param("userId") int userId);
    
    @Query("SELECT g FROM Goal g WHERE g.user.user_id = :userId AND g.status = :status")
    List<Goal> findByUserUserIdAndStatus(@Param("userId") int userId, @Param("status") String status);
    
    @Query("SELECT g FROM Goal g WHERE g.user.user_id = :userId AND g.category = :category")
    List<Goal> findByUserUserIdAndCategory(@Param("userId") int userId, @Param("category") String category);
    
    @Query("SELECT g FROM Goal g WHERE g.user.user_id = :userId ORDER BY g.priorityLevel DESC, g.createdAt DESC")
    List<Goal> findByUserIdOrderByPriority(@Param("userId") int userId);
}
