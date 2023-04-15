package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/login")
    public UserWithoutPassDTO login(@RequestBody LoginDTO loginData, HttpSession s) {
        UserWithoutPassDTO u = userService.login(loginData);
        s.setAttribute("LOGGED", true);
        s.setAttribute("LOGGED_ID", u.getId());
        return u;
    }

    @PostMapping("/users/logout")
    public void logout(HttpSession s) {
        if (s.isNew()) {
            throw new UnauthorizedException("You have to login");
        }
        s.invalidate();
    }

    @PostMapping("/users")
    public UserWithoutPassDTO register(@RequestBody UserRegisterDTO registerData) {
        return userService.register(registerData);
    }

    @GetMapping("/users/{id}")
    public UserWithoutPassDTO getById(@PathVariable int id) {
        return userService.getById(id);
    }


    @PutMapping("/users")
    public UserWithoutPassDTO updatePassword(@RequestBody ChangePassDTO updateData, HttpSession s) {
        int userId = getLoggedId(s);
        return userService.changePass(updateData, userId);
    }


    @GetMapping("/users")
    public List<UserWithoutPassDTO> getAll() {
        return userService.getAll();
    }

    @DeleteMapping("/users")
    public String deleteProfile(HttpSession s) {
        int userId = getLoggedId(s);
        userService.deleteProfile(userId);
        s.invalidate();
        return "You have deleted your profile " + userId;
    }

    @PostMapping("/users/{id}/followers")
    public int follow(@PathVariable("id") int followedId, HttpSession s) {
        int followerId = getLoggedId(s);
        if (followedId == followerId) {
            throw new BadRequestException("Cannot follow this user.");
        }
        return userService.follow(followerId, followedId);
    }

    @DeleteMapping("/users/{id}/followers")
    public String unfollow(@PathVariable("id") int unfollowId, HttpSession s) {
        int userId = getLoggedId(s);
        userService.unfollow(unfollowId, userId);
        return "unfollowed_id: " + unfollowId;
    }

    @PutMapping("/users/profile")
    public UserWithoutPassDTO updateProfile(@RequestBody UpdateProfileDTO dto, HttpSession s) {
        int userId = getLoggedId(s);
        return userService.updateProfile(dto, userId);
    }

    @GetMapping("/users/{id}/followers")
    public UserWithFollowersDTO getUsersFollowers(@PathVariable("id") int userId, HttpSession s) {
        int reqId = getLoggedId(s);
        return userService.getUsersFollowers(userId);
    }
    @GetMapping("/users/books/{id}")
    public Set<UserWithoutPassDTO> getUsersByBook(@PathVariable("id") int bookId, HttpSession s){
        int userId=getLoggedId(s);
       return userService.getUserByBook(bookId);
    }
}
