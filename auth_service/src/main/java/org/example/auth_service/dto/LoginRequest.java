package org.example.auth_service.dto;

import com.nimbusds.jose.shaded.jcip.NotThreadSafe;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "email is required")
    @Email(message="Enter the valid email")
    private String email;

    @NotBlank(message="password is required")
    private String password;
}
