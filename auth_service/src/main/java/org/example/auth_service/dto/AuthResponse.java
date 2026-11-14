package org.example.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
public class AuthResponse {

    private String accessToken;

    private String tokenType;

    private long expiresIn;

    public String getAccessToken(){
        return accessToken;
    }

    public String getTokenType(){
        return tokenType;
    }

    public long getExpiresIn(){
        return expiresIn;
    }
}
