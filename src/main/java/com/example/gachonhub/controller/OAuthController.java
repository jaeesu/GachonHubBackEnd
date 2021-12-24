package com.example.gachonhub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class OAuthController {

    @GetMapping("/oauth")
    public void test(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        log.info("\n\ngithub oauth token => {}", token);
        response.sendRedirect("http://localhost:8080/ranking");

    }
}
