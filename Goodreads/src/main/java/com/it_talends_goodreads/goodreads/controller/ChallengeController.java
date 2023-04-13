package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithOwnerInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.SetChallengeDTO;

import com.it_talends_goodreads.goodreads.model.entities.Challenge;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.service.ChallengeService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChallengeController extends AbstractController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/challenges")
    public ChallengeWithoutOwnerDTO create(@RequestBody SetChallengeDTO setChallengeDTO, HttpSession session) {
        boolean logged = (boolean) session.getAttribute("LOGGED");
        if (logged) {
            int userId = getLoggedId(session);
            return challengeService.createChallenge(setChallengeDTO, userId);
        }
        throw new UnauthorizedException("You have to login");
    }


    @PutMapping("/challenges/{id}")
    public ChallengeWithoutOwnerDTO updateChallenge(@RequestBody SetChallengeDTO setChallengeDTO, @PathVariable("id") int id, HttpSession session) {

        boolean logged = (boolean) session.getAttribute("LOGGED");
        if (logged) {
            int userId = getLoggedId(session);
            return challengeService.updateChallenge(id, setChallengeDTO, userId);
        }
        throw new UnauthorizedException("You have to login");

    }

    @DeleteMapping("/challenges/{id}")
    public String deleteChallenge(@PathVariable("id") int id, HttpSession session) {

        boolean logged = (boolean) session.getAttribute("LOGGED");
        if (logged) {
            int userId = getLoggedId(session);
            challengeService.deleteChallenge(id, userId);
            return "You delete challenge with id " +id;
        }
        throw new UnauthorizedException("You have to login");

    }

    @GetMapping("/challenges/{id}")
    public ChallengeWithOwnerInfoDTO getChallenge(@PathVariable("id") int id, HttpSession session) {
            return challengeService.getChallenge(id);
        }
    @GetMapping("/challenges")
    public List<ChallengeWithoutOwnerDTO> getAllMineChallenges(HttpSession session) {
        boolean logged = (boolean) session.getAttribute("LOGGED");
        if (logged) {
            int userId = getLoggedId(session);
            return challengeService.getAllMineChallenges(userId);
        }
        throw new UnauthorizedException("You have to login");

    }

}
