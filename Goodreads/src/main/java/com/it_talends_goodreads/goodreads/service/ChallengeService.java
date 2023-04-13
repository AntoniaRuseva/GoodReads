package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithOwnerInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.SetChallengeDTO;
import com.it_talends_goodreads.goodreads.model.entities.Challenge;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChallengeService extends AbstractService {
    @Autowired
    private ChallengeRepository challengeRepository;

    public ChallengeWithoutOwnerDTO createChallenge(SetChallengeDTO setChallengeDTO, int userId) {
        User user = getUserById(userId);
        if (challengeRepository.countByUserAndYear(userId) != 0) {
            throw new BadRequestException("You already have challenge for this year.");
        }
        if (setChallengeDTO.getNumber() <= 0 || setChallengeDTO.getNumber() > 1000) {
            throw new BadRequestException("The number of books for challenge has to be positive and less then 1000.");
        }

        Challenge challenge =  new Challenge();
        challenge.setUser(user);
        challenge.setNumber(setChallengeDTO.getNumber());
        challenge.setDateAdded(LocalDate.now());
        challengeRepository.save(challenge);
        return mapper.map(challenge, ChallengeWithoutOwnerDTO.class);

    }

    public ChallengeWithoutOwnerDTO updateChallenge(int challengeId, SetChallengeDTO setChallengeDTO, int userId) {

        Optional<Challenge> optional = challengeRepository.findById(challengeId);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such challenge");
        }

        if (userId != optional.get().getUser().getId()) {
            throw new UnauthorizedException("This challenge is not yours. It is not allowed to update it.");
        }

        if (setChallengeDTO.getNumber() <= 0 || setChallengeDTO.getNumber() > 1000) {
            throw new BadRequestException("The number of books for challenge has to be positive and less then 1000.");
        }
        optional.get().setNumber(setChallengeDTO.getNumber());
        return mapper.map(optional.get(), ChallengeWithoutOwnerDTO.class);
    }

    public void deleteChallenge(int id, int userId) {
        Optional<Challenge> optional = challengeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such challenge");
        }
        if (userId != optional.get().getUser().getId()) {
            throw new UnauthorizedException("This challenge is not yours. It is not allowed to update it.");
        }
        challengeRepository.deleteById(id);

    }

    public ChallengeWithOwnerInfoDTO getChallenge(int id) {
        Optional<Challenge> optional = challengeRepository.findById(id);
        if (optional.isEmpty()) {
            throw new BadRequestException("No such challenge");
        }
        Challenge challenge = optional.get();

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
        List<ChallengeWithoutOwnerDTO> list =  challengeList
                .stream()
                .map(ch -> mapper.map(ch, ChallengeWithoutOwnerDTO.class))
                .collect(Collectors.toList());
        if(list.isEmpty()){
            throw new NotFoundException("You don't have challenges");
        }
        return list;
    }
}
