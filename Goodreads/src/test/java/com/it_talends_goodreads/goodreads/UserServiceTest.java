package com.it_talends_goodreads.goodreads;


import com.it_talends_goodreads.goodreads.model.DTOs.*;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;
import com.it_talends_goodreads.goodreads.model.repositories.UserRepository;
import com.it_talends_goodreads.goodreads.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GoodreadsApplication.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    public ModelMapper mapper;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ShelfRepository shelfRepository;

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
        // Mocking the repository to return true when checking if the email already exists
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        // Creating a user register DTO with a strong password
        UserRegisterDTO registerData = getValidRegisterData();
        // Calling the register method should throw a BadRequestException
        userService.register(registerData);
    }

    //
    @Test
    public void testLoginSuccess() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setId(1);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.matches(password, user.getPassword())).thenReturn(true);

        UserWithoutPassDTO userWithoutPassDTO = new UserWithoutPassDTO();
        userWithoutPassDTO.setId(user.getId());
        userWithoutPassDTO.setEmail(user.getEmail());
        when(mapper.map(user, UserWithoutPassDTO.class)).thenReturn(userWithoutPassDTO);

        LoginDTO loginData = new LoginDTO();
        loginData.setEmail(email);
        loginData.setPassword(password);
        // Act
        UserWithoutPassDTO result = userService.login(loginData);
        // Assert
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
        verify(encoder, times(1)).matches(password, user.getPassword());
        verify(mapper, times(1)).map(user, UserWithoutPassDTO.class);
    }

    @Test(expected = UnauthorizedException.class)
    public void testLoginUserNotFound() {
        // Arrange
        String email = "test@example.com";
        LoginDTO loginData = new LoginDTO();
        loginData.setEmail(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        // Act
        userService.login(loginData);
    }

    @Test(expected = UnauthorizedException.class)
    public void testLoginUnauthorized() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode("wrong_password"));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.matches(password, user.getPassword())).thenReturn(false);

        LoginDTO loginData = new LoginDTO();
        loginData.setEmail(email);
        loginData.setPassword(password);
        // Act
        userService.login(loginData);
    }

    @Test
    public void testChangePassSuccess() {
        // Arrange
        int userId = 1;
        String oldPassword = "old_password";
        String newPassword = "new_password";
        String confirmNewPassword = "new_password";
        ChangePassDTO changePassDTO = new ChangePassDTO();
        changePassDTO.setCurrentPass(oldPassword);
        changePassDTO.setNewPass(newPassword);
        changePassDTO.setConfirmNewPass(confirmNewPassword);

        User user = new User();
        user.setId(userId);
        user.setPassword(encoder.encode(oldPassword));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(encoder.encode(newPassword)).thenReturn("encoded_new_password");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setPassword("encoded_new_password");
        when(userRepository.save(user)).thenReturn(updatedUser);

        UserWithoutPassDTO expected = new UserWithoutPassDTO();
        expected.setId(userId);
        expected.setEmail(user.getEmail());
        when(mapper.map(updatedUser, UserWithoutPassDTO.class)).thenReturn(expected);
        // Act
        UserWithoutPassDTO result = userService.changePass(changePassDTO, userId);
        // Assert
        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
        verify(encoder, times(1)).encode(newPassword);
        verify(mapper, times(1)).map(updatedUser, UserWithoutPassDTO.class);
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
        // Arrange
        int userId = 1;
        String oldPassword = "old_password";
        String newPassword = "new_password";
        String confirmNewPassword = "new_password";
        ChangePassDTO changePassDTO = new ChangePassDTO();
        changePassDTO.setCurrentPass(oldPassword);
        changePassDTO.setNewPass(newPassword);
        changePassDTO.setConfirmNewPass(confirmNewPassword);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        // Act
        userService.changePass(changePassDTO, userId);
    }

    @Test
    public void testDeleteProfileSuccess() {
        // Arrange
        int userId = 1;
        User user = new User();
        user.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        // Act
        userService.deleteProfile(userId);
        // Assert
        verify(shelfRepository, times(1)).deleteAllByUser(user);
        verify(userRepository, times(1)).delete(user);
    }

    @Test(expected = NotFoundException.class)
    public void testDeleteProfileUserNotFound() {
        // Arrange
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act
        userService.deleteProfile(userId);
    }
}



