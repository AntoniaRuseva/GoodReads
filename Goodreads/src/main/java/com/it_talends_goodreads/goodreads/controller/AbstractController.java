package com.it_talends_goodreads.goodreads.controller;

import com.it_talends_goodreads.goodreads.model.DTOs.ErrorDTO;
import com.it_talends_goodreads.goodreads.model.exceptions.BadRequestException;
import com.it_talends_goodreads.goodreads.model.exceptions.NotFoundException;
import com.it_talends_goodreads.goodreads.model.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
@RestController
public abstract class AbstractController {
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleBadRequest(Exception e) {
        return generateErrorDTO(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO handleUnauthorized(Exception e) {
        return generateErrorDTO(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFound(Exception e) {
        return generateErrorDTO(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleRest(Exception e) {
        return generateErrorDTO(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorDTO handleValidationExceptions(MethodArgumentNotValidException ex) {
        return generateErrorDTO(ex, HttpStatus.BAD_REQUEST);
    }

    private ErrorDTO generateErrorDTO(Exception e, HttpStatus s) {
        e.printStackTrace();
        ErrorDTO errorDTO = ErrorDTO.builder()
                .time(LocalDateTime.now())
                .status(s.value())
                .build();
        errorDTO.setException(e);
        return errorDTO;
    }

    protected int getLoggedId(HttpSession s) {
        if (s.getAttribute("LOGGED_ID") == null) {
            throw new UnauthorizedException("You have to login first!");
        }
        return (int) s.getAttribute("LOGGED_ID");
    }
}

