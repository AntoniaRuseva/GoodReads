package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.*;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ChallengeRepository;
import com.it_talends_goodreads.goodreads.model.repositories.FriendRepository;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;
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
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private ShelfRepository shelfRepository;

    @Transactional
    public ChallengeWithoutOwnerDTO createChallenge(CreateChallengeDTO setChallengeDTO, int userId) {
        User user = getUserById(userId);
        if (challengeRepository.countByUserAndYear(userId) != 0) {
            throw new BadRequestException("You already have challenge for this year.");
        }
        Challenge challenge = Challenge
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

    @Transactional
    public ChallengeProgressDTO getProgressByChallenge(int userId, int friendId, int challengeId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if(userId != friendId) {
            if(!user.getFriends().contains(friend)) {
                throw new UnauthorizedException("You can see only yours and your friends challenges.");
            }
        }
        User userToCheckProgress = getUserById(friendId);
        Challenge challenge = challengeRepository
                .findById(challengeId)
                .orElseThrow(() -> new  NotFoundException("No such challenge"));

        challengeRepository.getByUser(userToCheckProgress)
                .orElseThrow(() -> new BadRequestException("This challenge doesn't belong to this user"));

        int challengeTarget = challenge.getNumber();
        int challengeYear = challenge.getDateAdded().getYear();

        Optional<Shelf> optionalShelf = shelfRepository.findByUserAndName(userToCheckProgress, "Read");
        if(optionalShelf.isEmpty()){
            return ChallengeProgressDTO
                    .builder()
                    .userName(userToCheckProgress.getUserName())
                    .currentReadBook(0)
                    .target(challengeTarget)
                    .challengeYear(challengeYear).build();
        }
        Shelf shelf = optionalShelf.get();
        List<Book> readBooks = shelf.getBooksShelves().stream().map(BooksShelves::getBook).toList();

        return ChallengeProgressDTO
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
    }
}
