package com.it_talends_goodreads.goodreads;


import com.it_talends_goodreads.goodreads.model.DTOs.UserRegisterDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.UserWithoutPassDTO;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.repositories.UserRepository;
import com.it_talends_goodreads.goodreads.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GoodreadsApplication.class)
public class UserServiceTest {
    @Autowired
    public JavaMailSender javaMailSender;
    @Autowired
    private UserService userService;
    @Mock
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
    @Test
    public void testRegisterSuccess() {
        // Creating a user register DTO

        UserRegisterDTO registerData = new UserRegisterDTO();
        registerData.setEmail("test3@example.com");
        registerData.setUsername("testuser");
        registerData.setPassword("testpassword");
        registerData.setConfirmPassword("testpassword");
        registerData.setFirstName("John");
        registerData.setLastName("Doe");
        registerData.setAboutMe("I am a book lover");
        registerData.setLinkToSite("https://www.example.com");
        registerData.setGender("Male");


        Optional<User> user = userRepository.findByEmail(registerData.getEmail());
        user.ifPresent(value -> userRepository.delete(value));
        // Calling the register method
        UserWithoutPassDTO result = userService.register(registerData);

        // Asserting the result
        assertNotNull(result);
        assertEquals("test3@example.com", result.getEmail());
        assertEquals("testuser", result.getUsername());
        assertNull(result.getProfilePhoto());
        userRepository.deleteById(result.getId());
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
    @Test(expected = BadRequestException.class)
    public void testRegisterWeakPassword() {
        UserRegisterDTO registerData = getValidRegisterData();
        registerData.setPassword("weakPassword");
        registerData.setConfirmPassword("weakPassword");
        // Calling the register method should throw a ConstraintViolationException
        userService.register(registerData);
    }
}


