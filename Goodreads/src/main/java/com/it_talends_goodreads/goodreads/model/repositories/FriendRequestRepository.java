package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.DTOs.FriendRequestDTO;
import com.it_talends_goodreads.goodreads.model.entities.FriendRequest;
import com.it_talends_goodreads.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest,Integer> {
    boolean existsByRequesterAndReceiver(User requester, User receiver);

    Optional<FriendRequest> findByRequesterAndReceiver(User requester, User receiver);
    List<FriendRequest> findAllByReceiverId(int id);
}
