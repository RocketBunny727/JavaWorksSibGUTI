package org.example.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.model.AuthUser;
import org.example.model.Token;
import org.example.repository.IAuthUserRepository;
import org.example.repository.ITokenRepository;
import org.example.security.jwt.JwtProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final IAuthUserRepository authUserRepository;
    private final JwtProperties jwtProperties;
    private final ITokenRepository tokenRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }

    public String getLoginFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public UserDetails getAuthentication(String token) {
        String login = getLoginFromToken(token);
        AuthUser user = authUserRepository.findByLogin(login).orElseThrow();
        return User
                .withUsername(user.getLogin())
                .password(user.getPassword())
                .roles(user.getUserType().name())
                .build();
    }

    public Token generateToken(AuthUser user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusDays(jwtProperties.getExpiration());

        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        Date expiration = Date.from(expiryDate.atZone(ZoneId.systemDefault()).toInstant());

        String jwtToken = Jwts.builder()
                .setSubject(user.getLogin())
                .claim("role", user.getUserType().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256)
                .compact();

        Token token = new Token();
        token.setToken(jwtToken);
        token.setExpirationDate(expiryDate);
        token.setUser(user);
        token.setRevoked(false);

        return tokenRepository.save(token);
    }
}
