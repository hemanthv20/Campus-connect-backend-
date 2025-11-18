package com.socialmediaweb.socialmediaweb.dto;

import com.socialmediaweb.socialmediaweb.entities.Users;

public class UserMapper {
    
    // Convert Users entity to UserDTO
    public static UserDTO toDTO(Users user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUser_id());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirst_name());
        dto.setLastName(user.getLast_name());
        dto.setProfilePicture(user.getProfile_picture());
        dto.setCollege(user.getCollege());
        
        return dto;
    }
    
    // Convert Users entity to UserDTO with follow status
    public static UserDTO toDTO(Users user, boolean isFollowing, boolean isFollower, boolean isMutualFollow) {
        UserDTO dto = toDTO(user);
        if (dto != null) {
            dto.setFollowing(isFollowing);
            dto.setFollower(isFollower);
            dto.setMutualFollow(isMutualFollow);
        }
        return dto;
    }
}
