package com.it_talends_goodreads.goodreads.model.DTOs;

import com.it_talends_goodreads.goodreads.model.entities.FriendRequest;
import com.it_talends_goodreads.goodreads.model.entities.Shelf;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithFriendRequestsDTO {
    private int id;
    private String email;
    private String username;
    private String profilePhoto;
    private List<FriendRequestDTO> friendRequests;
    private List<ShelfWithoutOwnerAndBooksDTO> shelves;
}
