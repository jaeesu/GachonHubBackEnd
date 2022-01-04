package com.example.gachonhub.controller;

import com.example.gachonhub.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    public final TokenProvider tokenProvider;

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
