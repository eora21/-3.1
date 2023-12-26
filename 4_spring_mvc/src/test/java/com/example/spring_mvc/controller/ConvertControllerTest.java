package com.example.spring_mvc.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ConvertController.class)
class ConvertControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void member() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/member?id={id}&age={age}", 201, 201);
        mockMvc.perform(request)
                .andExpect(jsonPath("$.id").value(201))
                .andExpect(jsonPath("$.age").value(200));

        request = MockMvcRequestBuilders
                .get("/member?id={id}&age={age}", -1, -1);
        mockMvc.perform(request)
                .andExpect(jsonPath("$.id").value(-1))
                .andExpect(jsonPath("$.age").value(0));

        request = MockMvcRequestBuilders
                .get("/member?id={id}&age={age}", 100, 200);
        mockMvc.perform(request)
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.age").value(200));
    }
}