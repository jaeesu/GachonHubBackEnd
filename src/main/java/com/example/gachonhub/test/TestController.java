package com.example.gachonhub.test;

import com.example.gachonhub.common.ui.out.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/any-role-test")
    public ResponseEntity<ResponseUtil.DefaultResponse<String>> anyRole() {
        return ResponseUtil.success("helo");
    }

    @GetMapping("/required-authorization-test")
    public String requiredAutuorization() {
        return "required authenticated test success";
    }
}
