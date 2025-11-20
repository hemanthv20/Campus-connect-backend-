package com.socialmediaweb.socialmediaweb.dto;

import java.util.List;

public class SearchResponseDTO {
    private List<SearchResultDTO> results;
    private int totalResults;
    private int currentPage;
    private int totalPages;
    private List<String> suggestedSkills;
    private List<String> suggestedInterests;
    
    public SearchResponseDTO() {}
    
    public SearchResponseDTO(List<SearchResultDTO> results, int totalResults, int currentPage, int totalPages) {
        this.results = results;
        this.totalResults = totalResults;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }
    
    // Getters and Setters
    public List<SearchResultDTO> getResults() { return results; }
    public void setResults(List<SearchResultDTO> results) { this.results = results; }
    
    public int getTotalResults() { return totalResults; }
    public void setTotalResults(int totalResults) { this.totalResults = totalResults; }
    
    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
    
    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
    
    public List<String> getSuggestedSkills() { return suggestedSkills; }
    public void setSuggestedSkills(List<String> suggestedSkills) { this.suggestedSkills = suggestedSkills; }
    
    public List<String> getSuggestedInterests() { return suggestedInterests; }
    public void setSuggestedInterests(List<String> suggestedInterests) { this.suggestedInterests = suggestedInterests; }
}
