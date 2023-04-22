package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.UserWithoutPassDTO;
import com.it_talends_goodreads.goodreads.service.MediaService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;


@RestController
public class MediaController extends AbstractController {
    @Autowired
    private MediaService mediaService;

    @PostMapping("/media")
    public UserWithoutPassDTO uploadUserPicture(@RequestParam("file") MultipartFile file, HttpSession s) {
        int userId = getLoggedId(s);
        return mediaService.uploadUserPicture(file, userId);
    }

    @GetMapping("/media/{fileName}")
    public void downloadUserPicture(@PathVariable String fileName, HttpServletResponse resp) throws IOException {
        File f = mediaService.downloadUserPicture(fileName);
        Files.copy(f.toPath(), resp.getOutputStream());
    }

    @GetMapping("/media/books/{bookName}")
    public void downloadBookPicture(@PathVariable String bookName, HttpServletResponse resp) throws IOException {
        File f = mediaService.downloadBookPicture(bookName);
        Files.copy(f.toPath(), resp.getOutputStream());
    }
}
