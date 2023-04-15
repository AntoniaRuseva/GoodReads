package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithOwnerInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateChallengeDTO;
import com.it_talends_goodreads.goodreads.model.entities.Challenge;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService extends AbstractService {
    @Autowired
    private ChallengeRepository challengeRepository;
    @Transactional
    public ChallengeWithoutOwnerDTO createChallenge(CreateChallengeDTO setChallengeDTO, int userId) {
        User user = getUserById(userId);
        if (challengeRepository.countByUserAndYear(userId) != 0) {
            throw new BadRequestException("You already have challenge for this year.");
        }
        Challenge challenge = new Challenge();
        challenge = Challenge
                .builder()
                .user(user)
                .number(setChallengeDTO.getNumber())
                .dateAdded(LocalDate.now())
                .build();
        challengeRepository.save(challenge);
        return mapper.map(challenge, ChallengeWithoutOwnerDTO.class);

    }
    @Transactional
    public ChallengeWithoutOwnerDTO updateChallenge(int challengeId, CreateChallengeDTO setChallengeDTO, int userId) {
        Challenge challenge = exists(challengeId);
        if (authorized(userId, challenge)) {
            challenge.setNumber(setChallengeDTO.getNumber());
            challengeRepository.save(challenge);

        }
        return mapper.map(challenge, ChallengeWithoutOwnerDTO.class);
    }
    @Transactional
    public void deleteChallenge(int id, int userId) {
        Challenge challenge = exists(id);
        if (authorized(userId, challenge)) {
            challengeRepository.deleteById(id);
        }
    }

    public ChallengeWithOwnerInfoDTO getChallenge(int id) {
        Challenge challenge = exists(id);
        return ChallengeWithOwnerInfoDTO
                .builder()
                .id(id)
                .number(challenge.getNumber())
                .dateAdded(challenge.getDateAdded())
                .ownerUserName(challenge.getUser().getUserName())
                .build();
    }

    public List<ChallengeWithoutOwnerDTO> getAllMineChallenges(int userId) {
        List<Challenge> challengeList = challengeRepository.getAllByUser(getUserById(userId));
        List<ChallengeWithoutOwnerDTO> list = challengeList
                .stream()
                .map(ch -> mapper.map(ch, ChallengeWithoutOwnerDTO.class))
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new NotFoundException("You don't have challenges");
        }
        return list;
    }

    private Challenge exists(int id) {
        Optional<Challenge> optional = challengeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such challenge");
        }
        return optional.get();
    }

    private boolean authorized(int userId, Challenge challenge) {
        if (userId != challenge.getUser().getId()) {
            throw new UnauthorizedException("You are not allowed to make changes");
        }
        return true;
    }


}
