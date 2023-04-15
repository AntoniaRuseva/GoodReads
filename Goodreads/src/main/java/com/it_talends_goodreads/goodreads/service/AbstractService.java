package com.it_talends_goodreads.goodreads.service;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.repositories.BookRepository;
import com.it_talends_goodreads.goodreads.model.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractService {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected BookRepository bookRepository;
    @Autowired
    protected ModelMapper mapper;
    protected User getUserById(int id) {
       return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User not found!"));
    }
}
