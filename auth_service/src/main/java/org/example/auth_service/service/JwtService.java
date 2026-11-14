package org.example.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.example.auth_service.Entity.User;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public static final Duration TOKEN_EXPIRATION = Duration.ofMinutes(15);

    public String generateToken(User user){
        Instant now = Instant.now();
        Instant expiry = now.plus(TOKEN_EXPIRATION);


        JwsHeader header = JwsHeader
                .with(SignatureAlgorithm.RS256)
                .build();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("ticketsync-auth-service")
                .issuedAt(now)
                .expiresAt(expiry)
                .subject(user.getEmail())
                .claim("userId",user.getUserId())
                .claim("role",user.getRole().name())
                .build();

                return jwtEncoder.encode(
                        JwtEncoderParameters.from(header,claims)
                ).getTokenValue();

    }
    public long getExpirationTime(){
        return TOKEN_EXPIRATION.toSeconds();
    }


}
