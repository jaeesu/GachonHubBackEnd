package com.example.gachonhub.controller;

import com.example.gachonhub.exception.BadRequestException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.response.ResponseUtil;
import com.example.gachonhub.payload.response.ResponseUtil.DefaultResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceController {


    @ExceptionHandler(value = {BadRequestException.class, BindException.class})
    public ResponseEntity<DefaultResponse<String>> BadRequestHandler(Exception e, BindingResult result) {
        return ResponseUtil.fail(HttpStatus.BAD_REQUEST, result.getFieldError().getDefaultMessage());
        //e.getmessage와의 차이점 확인하기
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<DefaultResponse<String>> ResourceNotFoundHandler(Exception e) {
        return ResponseUtil.fail(HttpStatus.BAD_REQUEST, e.getMessage());
        //e.getmessage와의 차이점 확인하기
    }

}
