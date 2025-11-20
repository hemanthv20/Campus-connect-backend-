package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    
    @Query("SELECT e FROM Experience e WHERE e.user.user_id = :userId")
    List<Experience> findByUserUserId(@Param("userId") int userId);
    
    @Query("SELECT e FROM Experience e WHERE e.user.user_id = :userId ORDER BY e.displayOrder ASC, e.startDate DESC")
    List<Experience> findByUserIdOrderByDisplayOrder(@Param("userId") int userId);
    
    @Query("SELECT e FROM Experience e WHERE e.user.user_id = :userId AND e.experienceType = :experienceType")
    List<Experience> findByUserUserIdAndExperienceType(@Param("userId") int userId, @Param("experienceType") String experienceType);
}
