package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.entities.Friend;
import com.it_talends_goodreads.goodreads.model.entities.FriendID;
import com.it_talends_goodreads.goodreads.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendID> {
    Optional<Friend> findByFriend_IdAndUser(int friendId, User user);
}
