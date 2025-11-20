package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    @Query("SELECT p FROM Project p WHERE p.user.user_id = :userId")
    List<Project> findByUserUserId(@Param("userId") int userId);
    
    @Query("SELECT p FROM Project p WHERE p.user.user_id = :userId ORDER BY p.displayOrder ASC, p.createdAt DESC")
    List<Project> findByUserIdOrderByDisplayOrder(@Param("userId") int userId);
    
    @Query("SELECT p FROM Project p WHERE p.user.user_id = :userId AND p.isFeatured = true")
    List<Project> findFeaturedProjectsByUserId(@Param("userId") int userId);
    
    @Query("SELECT p FROM Project p WHERE p.user.user_id = :userId AND p.status = :status")
    List<Project> findByUserUserIdAndStatus(@Param("userId") int userId, @Param("status") String status);
}
