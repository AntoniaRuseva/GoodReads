package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.LoginDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.UpdateProfileDto;
import com.it_talends_goodreads.goodreads.model.DTOs.UserRegisterDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.UserWithoutPassDTO;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends AbstractService {
    @Autowired
    private BCryptPasswordEncoder encoder;

    public UserWithoutPassDTO login(LoginDTO loginData) {
        Optional<User> u = userRepository.findByEmail(loginData.getEmail());
        if (u.isEmpty() || !encoder.matches(loginData.getPassword(), u.get().getPassword())) {
            throw new UnauthorizedException("Wrong credentials");
        }
        return mapper.map(u, UserWithoutPassDTO.class);
    }

    public UserWithoutPassDTO register(UserRegisterDTO registerData) {//TODO strong password
        if (!registerData.getPassword().equals(registerData.getConfirmPassword())) {
            throw new BadRequestException("Mismatching password");
        }
        if (userRepository.existsByEmail(registerData.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        User u = mapper.map(registerData, User.class);
        u.setPassword(encoder.encode(u.getPassword()));
        userRepository.save(u);
        return mapper.map(u, UserWithoutPassDTO.class);

    }

    public UserWithoutPassDTO getById(int id) {
        Optional<User> u = userRepository.findById(id);
        if (u.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        return mapper.map(u.get(), UserWithoutPassDTO.class);
    }

    public UserWithoutPassDTO changePass(UpdateProfileDto updateData) {
        return null;//TODO
    }
}
