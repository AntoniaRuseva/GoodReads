package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        User u = getUserById(id);
        return mapper.map(u, UserWithoutPassDTO.class);
    }


    public UserWithoutPassDTO changePass(ChangePassDTO updateData, int userId) {
            if(!updateData.getNewPass().equals(updateData.getConfirmNewPass())){
                throw new BadRequestException("Mismatching password");
            }
            User u= new User();
            u.setPassword(encoder.encode(updateData.getNewPass()));
            userRepository.save(u);
        return mapper.map(u,UserWithoutPassDTO.class);
    }

    public List<UserWithoutPassDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> mapper.map(u, UserWithoutPassDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteProfile(int userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    public int follow(int followerId, int followedId) {
        User follower = getUserById(followerId);
        User followed = getUserById(followedId);
        followed.getFollowers().add(follower);
        userRepository.save(followed);
        return followed.getFollowers().size();
    }

    public void unfollow(int unfollowId, int userId) {
        User user = getUserById(userId);
        User unfollowed = getUserById(unfollowId);
        unfollowed.getFollowers().remove(user);
        userRepository.save(unfollowed);
    }

    public UserWithoutPassDTO updateProfile(UpdateProfileDTO dto, int userId) {
        User u=getUserById(userId);
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setUserName(dto.getUsername());
        u.setAboutMe(dto.getAboutMe());
        u.setGender(dto.getGender());
        u.setLinkToSite(dto.getLinkToSite());
        userRepository.save(u);
        return mapper.map(u,UserWithoutPassDTO.class);
    }
}
