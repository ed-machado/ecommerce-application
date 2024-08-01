package com.machado.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.machado.ecommerce.dto.LoginRequestDTO;
import com.machado.ecommerce.dto.RegisterRequestDTO;
import com.machado.ecommerce.entity.User;
import com.machado.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testRegister() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setName("Test User");
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("password");
        registerRequest.setDateOfBirth(LocalDate.of(2000, 1, 1));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(registerRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void testLogin() throws Exception {
        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        userRepository.save(user);

        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginRequest)))
                .andExpect(status().isOk());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
