package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    
    /**
     * Find user by username (case-sensitive)
     */
    Optional<Users> findByUsername(String username);
    
    /**
     * Find user by email (case-sensitive)
     */
    Optional<Users> findByEmail(String email);
    
    /**
     * Check if username exists (case-insensitive)
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE LOWER(u.username) = LOWER(:username)")
    boolean existsByUsernameIgnoreCase(@Param("username") String username);
    
    /**
     * Check if email exists (case-insensitive)
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM Users u WHERE LOWER(u.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);
    
    /**
     * Find user by username (case-insensitive)
     */
    @Query("SELECT u FROM Users u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<Users> findByUsernameIgnoreCase(@Param("username") String username);
    
    /**
     * Find user by email (case-insensitive)
     */
    @Query("SELECT u FROM Users u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Users> findByEmailIgnoreCase(@Param("email") String email);
}