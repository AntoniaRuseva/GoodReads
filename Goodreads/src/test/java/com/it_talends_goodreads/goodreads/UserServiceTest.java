package com.it_talends_goodreads.goodreads;


import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;
import com.it_talends_goodreads.goodreads.model.repositories.UserRepository;
import com.it_talends_goodreads.goodreads.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GoodreadsApplication.class)
@Transactional
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    public ModelMapper mapper;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;

    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    private UserRegisterDTO getValidRegisterData() {
        UserRegisterDTO registerData = new UserRegisterDTO();
        registerData.setEmail("test@example.com");
        registerData.setUsername("testuser");
        registerData.setPassword("123aaaA$");
        registerData.setConfirmPassword("123aaaA$");
        return registerData;
    }

    @Test(expected = BadRequestException.class)
    public void testRegisterPasswordsNotMatching() {
        // Creating a user register DTO with passwords that don't match
        UserRegisterDTO registerData = getValidRegisterData();
        registerData.setConfirmPassword("notMatchingPassword");
        // Calling the register method should throw a BadRequestException
        userService.register(registerData);
    }

    @Test(expected = BadRequestException.class)
    public void testRegisterEmailAlreadyExists() {
        UserRegisterDTO registerData = getValidRegisterData();
        userService.register(registerData);
        //Try to register an existing user again
        userService.register(registerData);
    }

    //
    @Test
    public void testLoginSuccess() {
        // Arrange
        UserRegisterDTO user = getValidRegisterData();
        userService.register(user);

        LoginDTO loginData = mapper.map(user,LoginDTO.class);
        UserWithoutPassDTO result = userService.login(loginData);
        // Assert
        assertTrue(result.getId() != 0);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test(expected = UnauthorizedException.class)
    public void testLoginUserNotFound() {
        // Arrange
        String email = "test@example.com";
        LoginDTO loginData = new LoginDTO();
        loginData.setEmail(email);
        userService.login(loginData);
    }

    @Test(expected = UnauthorizedException.class)
    public void testLoginUnauthorized() {
        UserRegisterDTO userRegisterDTO = getValidRegisterData();

        LoginDTO loginData = new LoginDTO();
        loginData.setEmail(userRegisterDTO.getEmail());
        loginData.setPassword(userRegisterDTO.getPassword() + 1);
        userService.register(userRegisterDTO);
        userService.login(loginData);
    }

    @Test
    public void testChangePassSuccess() {
        String oldPassword = "123aaaA$";
        String newPassword = "new_password";
        ChangePassDTO changePassDTO = new ChangePassDTO();
        changePassDTO.setCurrentPass(oldPassword);
        changePassDTO.setNewPass(newPassword);
        changePassDTO.setConfirmNewPass(newPassword);
        UserWithoutPassDTO u = userService.register(getValidRegisterData());
        userService.changePass(changePassDTO, u.getId());
        assertTrue(encoder.matches(newPassword, userRepository.findById(u.getId()).get().getPassword()));
    }

    @Test(expected = BadRequestException.class)
    public void testChangePassMismatchedPassword() {
        // Arrange
        int userId = 1;
        String oldPassword = "old_password";
        String newPassword = "new_password";
        String confirmNewPassword = "wrong_password";
        ChangePassDTO changePassDTO = new ChangePassDTO();
        changePassDTO.setCurrentPass(oldPassword);
        changePassDTO.setNewPass(newPassword);
        changePassDTO.setConfirmNewPass(confirmNewPassword);
        // Act
        userService.changePass(changePassDTO, userId);
    }

    @Test(expected = NotFoundException.class)
    public void testChangePassUserNotFound() {
        String oldPassword = "123aaaA$";
        String newPassword = "new_password";
        String confirmNewPassword = "new_password";
        ChangePassDTO changePassDTO = new ChangePassDTO();
        changePassDTO.setCurrentPass(oldPassword);
        changePassDTO.setNewPass(newPassword);
        changePassDTO.setConfirmNewPass(confirmNewPassword);
        userService.register(getValidRegisterData());
        userService.changePass(changePassDTO, 2);
    }
    @Test
    public void testDeleteProfile() {
        UserWithoutPassDTO user = userService.register(getValidRegisterData());
        userService.deleteProfile(user.getId());
        UserWithFriendRequestsDTO a = userService.getById(user.getId());
        assertEquals("deleted", a.getEmail());
    }
}



