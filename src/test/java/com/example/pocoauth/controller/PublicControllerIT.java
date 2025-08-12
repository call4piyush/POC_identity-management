package com.example.pocoauth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PublicControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void successEndpoint_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/public/success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    void fallbackEndpoint_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/public/fallback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("fallback"));
    }
}


