package com.socialmediaweb.socialmediaweb.controller;

import com.socialmediaweb.socialmediaweb.dto.*;
import com.socialmediaweb.socialmediaweb.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    
    @Autowired
    private SearchService searchService;
    
    // Advanced search with filters
    @PostMapping("/advanced")
    public ResponseEntity<SearchResponseDTO> advancedSearch(
            @RequestBody SearchCriteriaDTO criteria,
            @RequestParam int userId) {
        try {
            SearchResponseDTO response = searchService.advancedSearch(criteria, userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // Quick search for autocomplete
    @GetMapping("/quick")
    public ResponseEntity<List<SearchResultDTO>> quickSearch(
            @RequestParam String query,
            @RequestParam int userId) {
        try {
            List<SearchResultDTO> results = searchService.quickSearch(query, userId);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // Get personalized recommendations
    @GetMapping("/recommendations")
    public ResponseEntity<List<SearchResultDTO>> getRecommendations(@RequestParam int userId) {
        try {
            List<SearchResultDTO> recommendations = searchService.getRecommendations(userId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // Get trending/popular users
    @GetMapping("/trending")
    public ResponseEntity<List<SearchResultDTO>> getTrendingUsers(@RequestParam int userId) {
        try {
            // For now, return recommendations
            // In future, can be based on follower count, activity, etc.
            List<SearchResultDTO> trending = searchService.getRecommendations(userId);
            return ResponseEntity.ok(trending);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
