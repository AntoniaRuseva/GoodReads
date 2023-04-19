package com.it_talends_goodreads.goodreads.model.DTOs;

import com.it_talends_goodreads.goodreads.model.entities.FriendRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserWithFriendRequestsDTO {
    private int id;
    private String email;
    private String username;
    private String profilePhoto;
    private List<FriendRequestDTO> friendRequests;
}
