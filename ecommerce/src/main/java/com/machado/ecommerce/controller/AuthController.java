package com.machado.ecommerce.controller;

import com.machado.ecommerce.dto.LoginRequestDTO;
import com.machado.ecommerce.dto.RegisterRequestDTO;
import com.machado.ecommerce.dto.UserResponseDTO;
import com.machado.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO requestDTO) {
        UserResponseDTO responseDTO = authService.register(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        UserResponseDTO responseDTO = authService.login(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
