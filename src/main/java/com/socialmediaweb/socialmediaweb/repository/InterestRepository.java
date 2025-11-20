package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.Interest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
    
    Optional<Interest> findByName(String name);
    
    boolean existsByName(String name);
    
    List<Interest> findByCategoryId(Long categoryId);
    
    @Query("SELECT i FROM Interest i WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Interest> searchByName(String query);
    
    @Query("SELECT i FROM Interest i ORDER BY i.usageCount DESC")
    List<Interest> findTrendingInterests();
}
