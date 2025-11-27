package com.service.impl;

import com.model.dto.request.LoginRequest;
import com.model.dto.response.JwtResponse;
import com.model.entity.User;
import com.repository.UserRepository;
import com.security.JwtUtil;
import com.service.AuthService;
import lombok.RequiredArgsConstructor;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public JwtResponse login(LoginRequest request) {
        // Проверяем логин и пароль через стандартный AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Формируем ответ с токеном и краткой информацией о пользователе
        String token = jwtUtil.generateToken(userDetails);
        return JwtResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresAt(jwtUtil.getExpirationInstant(token))
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .build();
    }
}

