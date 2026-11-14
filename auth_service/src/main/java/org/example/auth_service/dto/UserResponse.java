package org.example.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.auth_service.Enum.Role;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String userName;

    private String email;

    private Role role;

    private LocalDateTime createdAt;
}
