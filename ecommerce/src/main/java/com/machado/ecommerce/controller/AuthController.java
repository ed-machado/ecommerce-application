package com.machado.ecommerce.controller;

import com.machado.ecommerce.dto.AuthRequest;
import com.machado.ecommerce.dto.RegisterRequestDTO;
import com.machado.ecommerce.dto.UserResponseDTO;
import com.machado.ecommerce.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.login(authRequest); // Use AuthRequest directly in login
    }
}
