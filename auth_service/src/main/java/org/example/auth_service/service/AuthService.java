package org.example.auth_service.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.auth_service.Entity.User;
import org.example.auth_service.Repository.UserRepository;
import org.example.auth_service.config.PasswordConfig;
import org.example.auth_service.dto.AuthResponse;
import org.example.auth_service.dto.LoginRequest;
import org.example.auth_service.dto.RegisterRequest;
import org.example.auth_service.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public UserResponse register(RegisterRequest request){
        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);
        if(userRepo.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email is already registered"
            );
        }
            User user = new User();
            user.setEmail(request.getEmail());
            user.setUserName(request.getUserName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());

            User savedUser = userRepo.save(user);

            return new UserResponse(
                    savedUser.getUserId(),
                    savedUser.getUserName(),
                    savedUser.getEmail(),
                    savedUser.getRole(),
                    savedUser.getCreatedAt()
            );
    }

    @Transactional
    public AuthResponse login(LoginRequest request){
        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user  = userRepo.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Wrong password"
            );
        }
        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,"Bearer",jwtService.getExpirationTime()
        );
    }
}
