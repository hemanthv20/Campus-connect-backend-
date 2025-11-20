package com.socialmediaweb.socialmediaweb.repository;

import com.socialmediaweb.socialmediaweb.entities.UserProfileExtension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileExtensionRepository extends JpaRepository<UserProfileExtension, Long> {
    @Query("SELECT upe FROM UserProfileExtension upe WHERE upe.user.user_id = :userId")
    Optional<UserProfileExtension> findByUserUserId(@Param("userId") int userId);
}
