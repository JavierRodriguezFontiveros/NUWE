package com.hackathon.blockchain.exceptions;

import com.hackathon.blockchain.response.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja excepciones específicas de la aplicación como 'UsernameAlreadyExistsException' o 'UserNotFoundException'
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleUsernameAlreadyExists(UsernameAlreadyExistsException e) {
        return new ResponseMessage("❌ " + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseMessage handleUserNotFound(UserNotFoundException e) {
        return new ResponseMessage("❌ " + e.getMessage());
    }

    // Para excepciones generales o no manejadas, devolvemos un error genérico
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseMessage handleGeneralException(Exception e) {
        return new ResponseMessage("❌ Ocurrió un error interno: " + e.getMessage());
    }
}
