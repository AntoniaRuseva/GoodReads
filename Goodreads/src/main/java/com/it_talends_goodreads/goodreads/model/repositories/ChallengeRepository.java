package com.it_talends_goodreads.goodreads.model.repositories;

import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.entities.Challenge;
import com.it_talends_goodreads.goodreads.model.entities.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {

    @Query(value = "SELECT COUNT(*) FROM challenges AS c WHERE YEAR(date_added) = YEAR(curdate()) AND user = ?1", nativeQuery = true)
    int countByUserAndYear(int ownerId);

    Optional<Challenge> findById(int id);

    void deleteById(int id);

    List<Challenge> getAllByUser(User user);

    Optional<Challenge> getByUser(User user);



}
