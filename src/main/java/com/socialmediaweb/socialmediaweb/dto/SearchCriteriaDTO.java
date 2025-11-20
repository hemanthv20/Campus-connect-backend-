package com.socialmediaweb.socialmediaweb.dto;

import java.util.List;

public class SearchCriteriaDTO {
    private String query;
    private List<String> skills;
    private List<String> interests;
    private List<String> goals;
    private String college;
    private String semester;
    private String batch;
    private String location;
    private String searchType; // "AND", "OR", "PHRASE"
    private Integer page;
    private Integer size;
    private String sortBy; // "relevance", "name", "recent"
    
    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }
    
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }
    
    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }
    
    public List<String> getGoals() { return goals; }
    public void setGoals(List<String> goals) { this.goals = goals; }
    
    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getBatch() { return batch; }
    public void setBatch(String batch) { this.batch = batch; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getSearchType() { return searchType; }
    public void setSearchType(String searchType) { this.searchType = searchType; }
    
    public Integer getPage() { return page != null ? page : 0; }
    public void setPage(Integer page) { this.page = page; }
    
    public Integer getSize() { return size != null ? size : 20; }
    public void setSize(Integer size) { this.size = size; }
    
    public String getSortBy() { return sortBy != null ? sortBy : "relevance"; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}
