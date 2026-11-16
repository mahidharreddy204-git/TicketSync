package org.example.auth_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.auth_service.dto.AuthResponse;
import org.example.auth_service.dto.LoginRequest;
import org.example.auth_service.dto.RegisterRequest;
import org.example.auth_service.dto.UserResponse;
import org.example.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class authController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){
            UserResponse userResponse = authService.register(request);
            return  ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt) {

        return Map.of(
                "userId", jwt.getClaim("userId"),
                "email", jwt.getSubject(),
                "role", jwt.getClaim("role"),
                "message", "JWT validation successful"
        );
    }

}
