package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.*;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ChallengeRepository;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeService extends AbstractService {
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ShelfRepository shelfRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChallengeService.class);

    public ChallengeWithoutOwnerDTO createChallenge(CreateChallengeDTO setChallengeDTO, int userId) {
        User user = getUserById(userId);
        if (challengeRepository.countByUserAndYear(userId) != 0) {
            throw new BadRequestException(String.format("User with id  %d is trying to make a new challenge, but there is already one." +
            " First has to erase the available challenge", userId));
        }
        Challenge challenge = Challenge
                .builder()
                .user(user)
                .number(setChallengeDTO.getNumber())
                .dateAdded(LocalDate.now())
                .build();
        challengeRepository.save(challenge);
        logger.info(String.format("User with id  %d created a new challenge with %d goal", userId, setChallengeDTO.getNumber()));
        return mapper.map(challenge, ChallengeWithoutOwnerDTO.class);

    }


    public ChallengeWithoutOwnerDTO updateChallenge(int challengeId, CreateChallengeDTO setChallengeDTO, int userId) {
        Challenge challenge = exists(challengeId);
        if (authorized(userId, challenge)) {
            challenge.setNumber(setChallengeDTO.getNumber());
            logger.info(String.format("User with id %d updated challenge with id %d. The challenge goal is %d now.",
                    userId, challengeId, setChallengeDTO.getNumber()));
            challengeRepository.save(challenge);
        }
        return mapper.map(challenge, ChallengeWithoutOwnerDTO.class);
    }


    public void deleteChallenge(int challengeId, int userId) {
        Challenge challenge = exists(challengeId);
        if (authorized(userId, challenge)) {
            logger.info(String.format("User with id %d deleted challenge with id %d.", userId, challengeId));
            challengeRepository.deleteById(challengeId);
        }
    }

    public ChallengeWithOwnerInfoDTO getChallenge(int id) {
        Challenge challenge = exists(id);
        return mapper.map(challenge,ChallengeWithOwnerInfoDTO.class);
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
        logger.info(String.format("User with id %d checks all his/her challenges. The result is %s",
                userId, list.stream().map(Object::toString)));
        return list;
    }

    private Challenge exists(int id) {
        return challengeRepository.findById(id).orElseThrow(() -> new BadRequestException("No such challenge"));
    }

    private boolean authorized(int userId, Challenge challenge) {
        if (userId != challenge.getUser().getId()) {
            throw new UnauthorizedException("You are not allowed to make changes");
        }
        return true;
    }


    public ChallengeProgressDTO getProgressByChallenge(int userId, int friendId, int challengeId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (userId != friendId) {
            if (!user.getFriends().contains(friend)) {
                throw new UnauthorizedException(String.format("User with id %d is trying to check the challenge progress to user with id %d" +
                                " without being his friend.",
                        userId, friendId));
            }
        }
        User userToCheckProgress = getUserById(friendId);
        Challenge challenge = challengeRepository
                .findById(challengeId)
                .orElseThrow(() -> new NotFoundException("No such challenge"));

        challengeRepository.getByUser(userToCheckProgress)
                .orElseThrow(() -> new BadRequestException("This challenge doesn't belong to this user"));

        int challengeTarget = challenge.getNumber();
        int challengeYear = challenge.getDateAdded().getYear();
        Shelf shelf = shelfRepository
                .findByUserAndName(userToCheckProgress, "Read")
                .orElseThrow(() -> new NotFoundException("You have deleted this shelf"));

        List<Book> readBooks = shelf.getBooksShelves().stream().map(BooksShelves::getBook).toList();

        ChallengeProgressDTO goalDTO = ChallengeProgressDTO
                .builder()
                .challengeId(challengeId)
                .userName(userToCheckProgress.getUserName())
                .currentReadBook(shelf.getBooksShelves().size())
                .target(challengeTarget)
                .challengeYear(challengeYear)
                .readBooksList(readBooks.stream().map(b -> BookCommonInfoDTO
                                .builder()
                                .id(b.getId())
                                .title(b.getTitle())
                                .authorName(b.getAuthor().getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
        logger.info(String.format("User with id %d checked the challenge progress to user with id %d. The result is %s",
                userId, friendId, goalDTO.toString()));
        return goalDTO;
    }
}
