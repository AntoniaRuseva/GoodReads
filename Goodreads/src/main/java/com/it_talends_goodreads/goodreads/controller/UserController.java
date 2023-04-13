package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.LoginDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.UpdateProfileDto;
import com.it_talends_goodreads.goodreads.model.DTOs.UserRegisterDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.UserWithoutPassDTO;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserController extends AbstractController {
    @Autowired
    private UserService userService;

    @PostMapping("/users/login")
    public UserWithoutPassDTO login(@RequestBody LoginDTO loginData,HttpSession s){
        UserWithoutPassDTO u=userService.login(loginData);
        s.setAttribute("LOGGED",true);
        s.setAttribute("LOGGED_ID",u.getId());
        return u;
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
    public UserWithoutPassDTO updateProfile(@RequestBody UpdateProfileDto updateData, HttpSession s) {
        boolean logged = (boolean) s.getAttribute("LOGGED");
        if(!logged){
            throw new UnauthorizedException("You have to login");
        }
        return userService.changePass(updateData);
    }

}
