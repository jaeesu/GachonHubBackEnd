package com.example.gachonhub.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
//@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ResourceNotFoundException extends RuntimeException {

    private String message;

    public ResourceNotFoundException() {
        super();
        this.message = "Resrouce Not Found";
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
