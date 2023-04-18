package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeProgressDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithOwnerInfoDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ChallengeWithoutOwnerDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.CreateChallengeDTO;


import com.it_talends_goodreads.goodreads.service.ChallengeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChallengeController extends AbstractController {

    @Autowired
    private ChallengeService challengeService;

    @PostMapping("/challenges")
    public ChallengeWithoutOwnerDTO create(@Valid @RequestBody CreateChallengeDTO setChallengeDTO, HttpSession session) {
        int userId = getLoggedId(session);
        return challengeService.createChallenge(setChallengeDTO, userId);
    }

    @PutMapping("/challenges/{id}")
    public ChallengeWithoutOwnerDTO update(@Valid @RequestBody CreateChallengeDTO setChallengeDTO, @PathVariable("id") int id, HttpSession session) {
        int userId = getLoggedId(session);
        return challengeService.updateChallenge(id
                , setChallengeDTO, userId);
    }

    @DeleteMapping("/challenges/{id}")
    public String delete(@PathVariable("id") int id, HttpSession session) {
            int userId = getLoggedId(session);
            challengeService.deleteChallenge(id, userId);
            return "You delete challenge with id " + id;
    }

    @GetMapping("/challenges/{id}")
    public ChallengeWithOwnerInfoDTO getById(@PathVariable("id") int id, HttpSession session) {
        return challengeService.getChallenge(id);
    }

    @GetMapping("/challenges")
    public List<ChallengeWithoutOwnerDTO> getAllMine(HttpSession session) {
            int userId = getLoggedId(session);
            return challengeService.getAllMineChallenges(userId);

    }

    @GetMapping("/users/{uid}/challenges/{cid}")
    public ChallengeProgressDTO getProgressByChallenge(@PathVariable("uid") int id,@PathVariable("cid") int challengeId, HttpSession session) {
        int userId = getLoggedId(session);
        return challengeService.getProgressByChallenge(userId, id, challengeId);

    }

}
