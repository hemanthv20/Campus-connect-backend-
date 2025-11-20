package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    
    @Query("SELECT e FROM Education e WHERE e.user.user_id = :userId")
    List<Education> findByUserUserId(@Param("userId") int userId);
    
    @Query("SELECT e FROM Education e WHERE e.user.user_id = :userId ORDER BY e.displayOrder ASC, e.startDate DESC")
    List<Education> findByUserIdOrderByDisplayOrder(@Param("userId") int userId);
}
