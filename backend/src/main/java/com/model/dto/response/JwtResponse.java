package com.model.dto.response;

import com.model.enums.UserRole;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class JwtResponse {
    String token;
    String tokenType;
    Instant expiresAt;
    String email;
    String fullName;
    UserRole role;
}

