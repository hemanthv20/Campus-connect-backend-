package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.GoalMilestone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalMilestoneRepository extends JpaRepository<GoalMilestone, Long> {
    
    List<GoalMilestone> findByGoalId(Long goalId);
    
    @Query("SELECT gm FROM GoalMilestone gm WHERE gm.goal.id = :goalId ORDER BY gm.displayOrder ASC, gm.targetDate ASC")
    List<GoalMilestone> findByGoalIdOrderByDisplayOrder(Long goalId);
}
