package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.*;

import com.it_talends_goodreads.goodreads.model.entities.Shelf;
import com.it_talends_goodreads.goodreads.model.entities.Book;
import com.it_talends_goodreads.goodreads.model.entities.BooksShelves;
import com.it_talends_goodreads.goodreads.model.entities.FriendRequest;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;
import com.it_talends_goodreads.goodreads.model.repositories.BooksShelvesRepository;
import com.it_talends_goodreads.goodreads.model.repositories.FriendRequestRepository;
import jakarta.mail.Message;
import jakarta.mail.internet.*;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService extends AbstractService {
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private ShelfRepository shelfRepository;
    @Autowired
    private BooksShelvesRepository booksShelvesRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    public UserWithoutPassDTO login(LoginDTO loginData) {
        Optional<User> u = userRepository.findByEmail(loginData.getEmail());
        if (u.isEmpty() || !encoder.matches(loginData.getPassword(), u.get().getPassword())) {
            throw new UnauthorizedException("Wrong credentials");
        }
        return mapper.map(u, UserWithoutPassDTO.class);
    }

    @Transactional
    public UserWithoutPassDTO register(UserRegisterDTO registerData) {
        if (!registerData.getPassword().equals(registerData.getConfirmPassword())) {
            throw new BadRequestException("Mismatching password");
        }
        if (userRepository.existsByEmail(registerData.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
        User u = mapper.map(registerData, User.class);

        u.setPassword(encoder.encode(u.getPassword()));
        userRepository.save(u);
        Shelf shelf1 = Shelf.builder().user(u).name("Read").build();
        Shelf shelf2 = Shelf.builder().user(u).name("Currently-reading").build();
        Shelf shelf3 = Shelf.builder().user(u).name("To-read").build();
        shelfRepository.save(shelf1);
        shelfRepository.save(shelf2);
        shelfRepository.save(shelf3);
        return mapper.map(u, UserWithoutPassDTO.class);

    }

    public UserWithoutPassDTO getById(int id) {
        User u = getUserById(id);
        return mapper.map(u, UserWithoutPassDTO.class);
    }

    @Transactional
    public UserWithoutPassDTO changePass(ChangePassDTO updateData, int userId) {
        if (!updateData.getNewPass().equals(updateData.getConfirmNewPass())) {
            throw new BadRequestException("Mismatching password");
        }
        User u = getUserById(userId);

        u.setPassword(encoder.encode(updateData.getNewPass()));
        userRepository.save(u);
        return mapper.map(u, UserWithoutPassDTO.class);
    }

    @Transactional
    public List<UserWithoutPassDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(u -> mapper.map(u, UserWithoutPassDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteProfile(int userId) {
        User user = getUserById(userId);
        userRepository.delete(user);
    }

    @Transactional
    public int follow(int followerId, int followedId) {
        if(followedId==followerId){
            throw new UnauthorizedException("You cannot follow yourself.");
        }
        User follower = getUserById(followerId);
        User followed = getUserById(followedId);
        followed.getFollowers().add(follower);
        userRepository.save(followed);
        return followed.getFollowers().size();
    }

    @Transactional
    public void unfollow(int unfollowId, int userId) {
        User user = getUserById(userId);
        User unfollowed = getUserById(unfollowId);
        if(!unfollowed.getFollowed().contains(user)){
            throw new NotFoundException("You are not following this user.");
        }
        unfollowed.getFollowers().remove(user);
        userRepository.save(user);
    }

    @Transactional
    public UserWithoutPassDTO updateProfile(UpdateProfileDTO dto, int userId) {
        User u = getUserById(userId);
        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setUserName(dto.getUsername());
        u.setAboutMe(dto.getAboutMe());
        u.setGender(dto.getGender());
        u.setLinkToSite(dto.getLinkToSite());
        userRepository.save(u);
        return mapper.map(u, UserWithoutPassDTO.class);
    }

    public UserWithFollowersDTO getUsersFollowers(int userId) {
        User u = getUserById(userId);
        return mapper.map(u, UserWithFollowersDTO.class);
    }

    public Set<UserWithoutPassDTO> getUserByBook(int bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(()-> new BadRequestException("Book doesn't exist."));
        List<BooksShelves> booksShelves = booksShelvesRepository.getByBook_Id(bookId);
        Set<UserWithoutPassDTO> returnUsers = new HashSet<>();
        for (BooksShelves b : booksShelves) {
            returnUsers.add(mapper.map(b.getShelf().getUser(), UserWithoutPassDTO.class));
        }
        if(returnUsers.isEmpty()){
            throw new NotFoundException("No users reading this book");
        }
        return returnUsers;
    }

    @Transactional
    public int addFriendRequest(int requesterId, int receiverId) {
        if(requesterId==receiverId){
            throw new UnauthorizedException("You cannot request that friendship.");
        }
        User requester = getUserById(requesterId);
        User receiver = getUserById(receiverId);

        if (requester.getFriends().contains(receiver)) {
            throw new BadRequestException("The user is already a friend.");
        }

        if (friendRequestRepository.existsByRequesterAndReceiver(requester, receiver)) {
            throw new BadRequestException("The request already exists.");
        }

        if (friendRequestRepository.existsByRequesterAndReceiver(receiver, requester)) {
            throw new BadRequestException("The request is already pending from the other side.");
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setReceiver(receiver);

        friendRequestRepository.save(friendRequest);

        return requesterId;
    }

    @Transactional
    public String acceptFriendRequest(int requesterId, int receiverId) {
        User requester = getUserById(requesterId);
        User receiver = getUserById(receiverId);

        FriendRequest friendRequest = checkRequestExists(requester, receiver);

        if (friendRequest.isRejected() || friendRequest.isAccepted()) {
            throw new BadRequestException("The request has already been"
                    + ((friendRequest.isRejected()) ? " rejected" : "accepted"));
        }

        requester.getFriends().add(receiver);
        receiver.getFriends().add(requester);

        receiver.getUsersFriends().add(requester);
        requester.getUsersFriends().add(receiver);
        friendRequest.setAccepted(true);

        friendRequestRepository.save(friendRequest);
        userRepository.save(requester);
        userRepository.save(receiver);
        return "User " + requesterId + " and user " + receiverId + " are now friends.";
    }

    @Transactional
    public String rejectFriendRequest(int userId, int friendId) {
        User friend = getUserById(friendId);
        User user = getUserById(userId);

        FriendRequest friendRequest = checkRequestExists(friend, user);
        if (friendRequest.isRejected() || friendRequest.isAccepted()) {
            throw new BadRequestException("This user doesn't want to be your friend"
                    + ((friendRequest.isRejected()) ? " rejected" : "accepted"));
        }
        friendRequest.setRejected(true);
        friendRequestRepository.save(friendRequest);
        return "You have rejected " + friendId + "'s friend request.";
    }

    @Transactional
    public String removeFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        if (!user.getFriends().contains(friend)) {
            throw new NotFoundException("User with id: " + friendId + " is not your friend.");
        }
        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        FriendRequest friendRequest = friendRequestRepository.findByRequesterAndReceiver(user, friend)
                .orElseThrow(()->new NotFoundException("Request is already deleted."));

        friendRequestRepository.delete(friendRequest);
        userRepository.save(user);
        userRepository.save(friend);
        return "You have removed user " + friendId + " successfully";
    }

    public List<UserWithoutPassDTO> getFriends(int userId) {
        User user = getUserById(userId);
        if (user.getFriends().isEmpty()) {
            throw new NotFoundException("User doesn't have any friends yet.");
        }
        return user.getFriends()
                .stream()
                .map(u -> mapper.map(u, UserWithoutPassDTO.class))
                .collect(Collectors.toList());
    }

    private FriendRequest checkRequestExists(User requester, User receiver) {
        return friendRequestRepository.findByRequesterAndReceiver(requester, receiver)
                .orElseThrow(()->new BadRequestException(
                        "Request with requester " + requester.getId()
                         + ", and receiver " + receiver.getId() + " doesn't exist"));
    }
    @SneakyThrows
    public MimeMessage sendNewTemporaryPassword(EmailDTO email) {
        try {
            User u = userRepository.findByEmail(email.getEmail())
                    .orElseThrow(()->new NotFoundException("No user with this email"));
            String tempPassword = generateRandomPassword(new Random().nextInt(8, 16));
            u.setPassword(new BCryptPasswordEncoder().encode(tempPassword));
            userRepository.save(u);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress("goodreadsITTalents@gmail.com"));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getEmail()));
            mimeMessage.setSubject("New Password");
            mimeMessage.setText("Your new password is " + tempPassword);
            return mimeMessage;
        } catch (RuntimeException e) {
            throw new NotFoundException("Problem with sending the email");
        }
    }

    private String generateRandomPassword(int length) {

        final String UPPER_CASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final String LOWER_CASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
        final String DIGITS = "0123456789";
        final String SPECIAL_CHARS = "@$!%*#?&";
        final String ALL_CHARS = UPPER_CASE_CHARS + LOWER_CASE_CHARS + DIGITS + SPECIAL_CHARS;

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        password.append(getRandomChar(UPPER_CASE_CHARS, random));
        password.append(getRandomChar(LOWER_CASE_CHARS, random));
        password.append(getRandomChar(DIGITS, random));
        password.append(getRandomChar(SPECIAL_CHARS, random));

        for (int i = 4; i < length + 4; i++) {
            password.append(getRandomChar(ALL_CHARS, random));
        }
        return shuffleString(password.toString());
    }

    private static char getRandomChar(String chars, SecureRandom random) {
        return chars.charAt(random.nextInt(chars.length()));
    }

    private static String shuffleString(String input) {
        List<Character> chars = new ArrayList<>();
        for (char c : input.toCharArray()) {
            chars.add(c);
        }
        Collections.shuffle(chars);
        StringBuilder shuffled = new StringBuilder(input.length());
        for (char c : chars) {
            shuffled.append(c);
        }
        return shuffled.toString();
    }

    public List<UserWithoutPassDTO> getAllByUserName(String userName) {
        List<User> users = userRepository.findAllByUserNameContaining(userName);
        if (users.isEmpty()) {
            throw new NotFoundException("User doesn't exist!");
        }
        return users
                .stream()
                .map(u -> mapper.map(u, UserWithoutPassDTO.class))
                .collect(Collectors.toList());
    }
}
