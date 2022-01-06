package com.example.gachonhub.controller;

import com.example.gachonhub.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/any-role-test")
    public String anyRole() {
        return "any role test success";
    }

    @GetMapping("/required-authorization-test")
    public String requiredAutuorization() {
        return "required authenticated test success";
    }
}
