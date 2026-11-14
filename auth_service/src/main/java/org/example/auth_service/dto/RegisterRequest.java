package org.example.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.auth_service.Enum.Role;

@Data
public class RegisterRequest {

    @NotBlank(message="userName is required")
    @Size(min= 3 , max=50,message="Name must be 33 and 50 characters")
    private String userName;


    @NotBlank(message="Password is required")
    @Size(min=4,max=8 ,message="Password must be 8 characters")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message="Enter the valid email")
    private String email;

    private Role role;
}
