package com.it_talends_goodreads.goodreads.service;

import com.it_talends_goodreads.goodreads.model.DTOs.UserWithoutPassDTO;
import com.it_talends_goodreads.goodreads.model.entities.User;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
public class MediaService extends AbstractService {

    public UserWithoutPassDTO uploadUserPicture(MultipartFile file, int userId) {
        try {
            Optional<User> u = userRepository.findById(userId);
            if (u.isEmpty()) {
                throw new NotFoundException("User not found!");
            }
            User user = u.get();

            String ext = FilenameUtils.getExtension(file.getOriginalFilename());
            String name = UUID.randomUUID().toString() + "." + ext;
            File dir = new File("uploads");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(dir, name);
            Files.copy(file.getInputStream(), f.toPath());
            String url = dir.getName() + File.separator + f.getName();
            user.setProfilePhoto(url);
            userRepository.save(user);

            return mapper.map(user, UserWithoutPassDTO.class);
        }
        catch (IOException e){
            throw new BadRequestException(e.getMessage());
        }
    }

    public File downloadUserPicture(String fileName) {
        File dir = new File("uploads");
        File f = new File(dir,fileName);
        if(f.exists()){
            return f;
        }
        throw new NotFoundException("File not found!");
    }

    public File downloadBookPicture(String bookName) {
        File dir = new File("bookPictures");
        File f = new File(dir,bookName);
        if(f.exists()){
            return f;
        }
        throw new NotFoundException("File not found!");
    }
}
