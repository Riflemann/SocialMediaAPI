package com.socialmedia.socialmediaapi.controller;

import com.socialmedia.socialmediaapi.dto.Response;
import com.socialmedia.socialmediaapi.exceptions.IncorrectIdException;
import com.socialmedia.socialmediaapi.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;

@ControllerAdvice
@Hidden
public class DefaultAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(UserNotFoundException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectIdException.class)
    public ResponseEntity<Response> handleIncorrectIdException(IncorrectIdException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Response> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        Response response = new Response("Указанный пользователь зарегистрирован");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
