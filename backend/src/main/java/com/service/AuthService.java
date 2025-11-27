package com.service;

import com.model.dto.request.LoginRequest;
import com.model.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
}

