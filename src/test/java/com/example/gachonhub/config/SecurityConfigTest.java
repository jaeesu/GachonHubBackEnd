package com.example.gachonhub.config;

import com.example.gachonhub.controller.TestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("스프링 시큐리티 인증 테스트")
class SecurityConfigTest {

    @Autowired
    TestController controller;

    @Autowired
    private MockMvc mockMvc;

//    @BeforeEach
//    public void init() {
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//    }

    @Test
    @DisplayName("permitAll()로 지정된 uri는 로그인없이 접근 가능")
    void permitAllClient() throws Exception {
        mockMvc.perform(get("/any-role-test"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("인증되지 않은 클라이언트가 api 요청 => 401 error")
    void unAuthorizedClient() throws Exception {
        mockMvc.perform(get("/required-authorization-test"))
                .andExpect(status().isUnauthorized());
    }

}