package com.socialmediaweb.socialmediaweb.service;

import com.socialmediaweb.socialmediaweb.dto.*;
import com.socialmediaweb.socialmediaweb.entities.*;
import com.socialmediaweb.socialmediaweb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {
    
    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private UserSkillRepository userSkillRepository;
    
    @Autowired
    private UserInterestRepository userInterestRepository;
    
    @Autowired
    private GoalRepository goalRepository;
    
    @Autowired
    private FollowRepository followRepository;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private InterestRepository interestRepository;
    
    public SearchResponseDTO advancedSearch(SearchCriteriaDTO criteria, int currentUserId) {
        List<Users> allUsers = usersRepository.findAll();
        List<SearchResultDTO> results = new ArrayList<>();
        
        for (Users user : allUsers) {
            if (user.getUser_id() == currentUserId) continue; // Skip current user
            
            double matchScore = calculateMatchScore(user, criteria, currentUserId);
            
            if (matchScore > 0) {
                SearchResultDTO result = convertToSearchResult(user, matchScore, currentUserId);
                results.add(result);
            }
        }
        
        // Sort by match score
        results.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        
        // Pagination
        int page = criteria.getPage();
        int size = criteria.getSize();
        int start = page * size;
        int end = Math.min(start + size, results.size());
        
        List<SearchResultDTO> paginatedResults = start < results.size() 
            ? results.subList(start, end) 
            : new ArrayList<>();
        
        int totalPages = (int) Math.ceil((double) results.size() / size);
        
        SearchResponseDTO response = new SearchResponseDTO(
            paginatedResults, 
            results.size(), 
            page, 
            totalPages
        );
        
        // Add suggestions
        response.setSuggestedSkills(getSuggestedSkills(criteria));
        response.setSuggestedInterests(getSuggestedInterests(criteria));
        
        return response;
    }
    
    private double calculateMatchScore(Users user, SearchCriteriaDTO criteria, int currentUserId) {
        double score = 0.0;
        StringBuilder matchReason = new StringBuilder();
        
        // Name/Username match (30 points)
        if (criteria.getQuery() != null && !criteria.getQuery().isEmpty()) {
            String query = criteria.getQuery().toLowerCase();
            String fullName = (user.getFirst_name() + " " + user.getLast_name()).toLowerCase();
            String username = user.getUsername().toLowerCase();
            
            if (fullName.contains(query) || username.contains(query)) {
                score += 30;
                matchReason.append("Name match. ");
            } else {
                return 0; // If query doesn't match name, skip this user
            }
        }
        
        // Skills match (25 points)
        if (criteria.getSkills() != null && !criteria.getSkills().isEmpty()) {
            List<UserSkill> userSkills = userSkillRepository.findByUserUserId(user.getUser_id());
            Set<String> userSkillNames = userSkills.stream()
                .map(us -> us.getSkill().getName().toLowerCase())
                .collect(Collectors.toSet());
            
            long matchingSkills = criteria.getSkills().stream()
                .filter(skill -> userSkillNames.contains(skill.toLowerCase()))
                .count();
            
            if (matchingSkills > 0) {
                score += (matchingSkills / (double) criteria.getSkills().size()) * 25;
                matchReason.append(matchingSkills).append(" matching skills. ");
            }
        }
        
        // Interests match (20 points)
        if (criteria.getInterests() != null && !criteria.getInterests().isEmpty()) {
            List<UserInterest> userInterests = userInterestRepository.findByUserUserId(user.getUser_id());
            Set<String> userInterestNames = userInterests.stream()
                .map(ui -> ui.getInterest().getName().toLowerCase())
                .collect(Collectors.toSet());
            
            long matchingInterests = criteria.getInterests().stream()
                .filter(interest -> userInterestNames.contains(interest.toLowerCase()))
                .count();
            
            if (matchingInterests > 0) {
                score += (matchingInterests / (double) criteria.getInterests().size()) * 20;
                matchReason.append(matchingInterests).append(" matching interests. ");
            }
        }
        
        // College match (15 points)
        if (criteria.getCollege() != null && !criteria.getCollege().isEmpty()) {
            if (user.getCollege() != null && 
                user.getCollege().toLowerCase().contains(criteria.getCollege().toLowerCase())) {
                score += 15;
                matchReason.append("Same college. ");
            }
        }
        
        // Semester/Batch match (10 points)
        if (criteria.getSemester() != null && user.getSemester() != null &&
            user.getSemester().equals(criteria.getSemester())) {
            score += 5;
            matchReason.append("Same semester. ");
        }
        
        if (criteria.getBatch() != null && user.getBatch() != null &&
            user.getBatch().equals(criteria.getBatch())) {
            score += 5;
            matchReason.append("Same batch. ");
        }
        
        return score;
    }
    
    private SearchResultDTO convertToSearchResult(Users user, double matchScore, int currentUserId) {
        SearchResultDTO result = new SearchResultDTO();
        result.setUserId(user.getUser_id());
        result.setUsername(user.getUsername());
        result.setFirstName(user.getFirst_name());
        result.setLastName(user.getLast_name());
        result.setProfilePicture(user.getProfile_picture());
        result.setCollege(user.getCollege());
        result.setSemester(user.getSemester());
        result.setBatch(user.getBatch());
        result.setBio(user.getBio());
        result.setMatchScore(matchScore);
        
        // Get user skills
        List<UserSkill> userSkills = userSkillRepository.findByUserUserId(user.getUser_id());
        result.setSkills(userSkills.stream()
            .map(us -> us.getSkill().getName())
            .limit(5)
            .collect(Collectors.toList()));
        
        // Get user interests
        List<UserInterest> userInterests = userInterestRepository.findByUserUserId(user.getUser_id());
        result.setInterests(userInterests.stream()
            .map(ui -> ui.getInterest().getName())
            .limit(5)
            .collect(Collectors.toList()));
        
        // Check if following
        Users follower = usersRepository.findById(currentUserId).orElse(null);
        result.setFollowing(follower != null && followRepository.existsByFollowerAndFollowing(follower, user));
        
        return result;
    }
    
    private List<String> getSuggestedSkills(SearchCriteriaDTO criteria) {
        if (criteria.getQuery() == null || criteria.getQuery().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Skill> skills = skillRepository.searchByName(criteria.getQuery());
        return skills.stream()
            .map(Skill::getName)
            .limit(5)
            .collect(Collectors.toList());
    }
    
    private List<String> getSuggestedInterests(SearchCriteriaDTO criteria) {
        if (criteria.getQuery() == null || criteria.getQuery().isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Interest> interests = interestRepository.searchByName(criteria.getQuery());
        return interests.stream()
            .map(Interest::getName)
            .limit(5)
            .collect(Collectors.toList());
    }
    
    // Quick search for autocomplete
    public List<SearchResultDTO> quickSearch(String query, int currentUserId) {
        if (query == null || query.length() < 2) {
            return new ArrayList<>();
        }
        
        List<Users> users = usersRepository.findAll();
        String lowerQuery = query.toLowerCase();
        
        return users.stream()
            .filter(user -> user.getUser_id() != currentUserId)
            .filter(user -> {
                String fullName = (user.getFirst_name() + " " + user.getLast_name()).toLowerCase();
                return fullName.contains(lowerQuery) || user.getUsername().toLowerCase().contains(lowerQuery);
            })
            .limit(10)
            .map(user -> convertToSearchResult(user, 100, currentUserId))
            .collect(Collectors.toList());
    }
    
    // Get recommendations based on user profile
    public List<SearchResultDTO> getRecommendations(int userId) {
        Users currentUser = usersRepository.findById(userId).orElse(null);
        if (currentUser == null) return new ArrayList<>();
        
        // Get user's skills and interests
        List<UserSkill> userSkills = userSkillRepository.findByUserUserId(userId);
        List<UserInterest> userInterests = userInterestRepository.findByUserUserId(userId);
        
        Set<String> userSkillNames = userSkills.stream()
            .map(us -> us.getSkill().getName())
            .collect(Collectors.toSet());
        
        Set<String> userInterestNames = userInterests.stream()
            .map(ui -> ui.getInterest().getName())
            .collect(Collectors.toSet());
        
        // Find similar users
        List<Users> allUsers = usersRepository.findAll();
        List<SearchResultDTO> recommendations = new ArrayList<>();
        
        for (Users user : allUsers) {
            if (user.getUser_id() == userId) continue;
            if (followRepository.existsByFollowerAndFollowing(currentUser, user)) continue;
            
            double similarityScore = calculateSimilarityScore(user, userSkillNames, userInterestNames, currentUser);
            
            if (similarityScore > 20) {
                SearchResultDTO result = convertToSearchResult(user, similarityScore, userId);
                result.setMatchReason(generateMatchReason(similarityScore));
                recommendations.add(result);
            }
        }
        
        recommendations.sort((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()));
        return recommendations.stream().limit(10).collect(Collectors.toList());
    }
    
    private double calculateSimilarityScore(Users user, Set<String> userSkills, Set<String> userInterests, Users currentUser) {
        double score = 0.0;
        
        // Skills similarity (40 points)
        List<UserSkill> targetSkills = userSkillRepository.findByUserUserId(user.getUser_id());
        long commonSkills = targetSkills.stream()
            .filter(us -> userSkills.contains(us.getSkill().getName()))
            .count();
        
        if (commonSkills > 0) {
            score += Math.min(commonSkills * 10, 40);
        }
        
        // Interests similarity (30 points)
        List<UserInterest> targetInterests = userInterestRepository.findByUserUserId(user.getUser_id());
        long commonInterests = targetInterests.stream()
            .filter(ui -> userInterests.contains(ui.getInterest().getName()))
            .count();
        
        if (commonInterests > 0) {
            score += Math.min(commonInterests * 10, 30);
        }
        
        // Same college (20 points)
        if (currentUser.getCollege() != null && currentUser.getCollege().equals(user.getCollege())) {
            score += 20;
        }
        
        // Same semester/batch (10 points)
        if (currentUser.getSemester() != null && currentUser.getSemester().equals(user.getSemester())) {
            score += 5;
        }
        if (currentUser.getBatch() != null && currentUser.getBatch().equals(user.getBatch())) {
            score += 5;
        }
        
        return score;
    }
    
    private String generateMatchReason(double score) {
        if (score >= 70) return "Highly compatible profile";
        if (score >= 50) return "Strong match based on skills and interests";
        if (score >= 30) return "Similar interests and background";
        return "Potential connection";
    }
}
