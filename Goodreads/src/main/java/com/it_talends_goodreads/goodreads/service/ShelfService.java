package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.CreateShelfDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ShelfWithOutUserDTO;
import com.it_talends_goodreads.goodreads.model.DTOs.ShelfWithoutOwnerAndBooksDTO;
import com.it_talends_goodreads.goodreads.model.entities.Shelf;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.repositories.ShelfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelfService extends AbstractService{
    @Autowired
    private ShelfRepository shelfRepository;
    public ShelfWithoutOwnerAndBooksDTO createShelf(CreateShelfDTO createShelfDTO, int userId) {
        User user = getUserById(userId);
        Shelf shelf = new Shelf();
        shelf.setUser(user);
        shelf.setName(createShelfDTO.getName());
        shelfRepository.save(shelf);
        return mapper.map(shelf, ShelfWithoutOwnerAndBooksDTO.class);
    }

    public List<ShelfWithOutUserDTO> getAllShelvesByUSer(int userId) {
        User user = getUserById(userId);
        List<Shelf> list = shelfRepository.findAllByUser(user);
        List<ShelfWithOutUserDTO> returnDTO = list.stream().map(s -> {
            return ShelfWithOutUserDTO.builder().id(s.getId()).name(s.getName()).countBooksInTheShelve(s.getBooksShelves().size()).build();
        }).toList();

        return returnDTO;
    }
}
