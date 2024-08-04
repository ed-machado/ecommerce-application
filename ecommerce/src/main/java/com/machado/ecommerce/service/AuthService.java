package com.machado.ecommerce.service;

import com.machado.ecommerce.dto.AuthRequest;
import com.machado.ecommerce.dto.LoginRequestDTO;
import com.machado.ecommerce.dto.RegisterRequestDTO;
import com.machado.ecommerce.dto.UserResponseDTO;
import com.machado.ecommerce.entity.User;
import com.machado.ecommerce.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {
        System.out.println("Registering user: " + requestDTO.getEmail());
        User user = new User();
        user.setName(requestDTO.getName());
        user.setEmail(requestDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(requestDTO.getPassword()));
        user.setDateOfBirth(requestDTO.getDateOfBirth());
        user.setRole("USER");
        userRepository.save(user);

        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }

    public boolean authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null && bCryptPasswordEncoder.matches(password, user.getPassword());
    }

    public ResponseEntity<UserResponseDTO> login(AuthRequest authRequest) { // Changed parameter to AuthRequest
        System.out.println("Authenticating user: " + authRequest.getEmail());
        boolean isAuthenticated = authenticateUser(authRequest.getEmail(), authRequest.getPassword());
        if (isAuthenticated) {
            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials !!"));

            UserResponseDTO responseDTO = new UserResponseDTO();
            responseDTO.setId(user.getId());
            responseDTO.setName(user.getName());
            responseDTO.setEmail(user.getEmail());

            responseDTO.setRole(user.getRole());
            responseDTO.setMessage("Login Successful");

            return ResponseEntity.ok(responseDTO);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserResponseDTO("Invalid credentials"));
        }
    }
}