package org.kun.intelligentcourse.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
@Getter
public class JWTService {

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.security.jwt.expiration}")
    private Long expiration;

    @Value("${spring.security.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private JwtParser  jwtParser;

    private SecretKey signKey;

    @jakarta.annotation.PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.signKey = Keys.hmacShaKeyFor(keyBytes);
        this.jwtParser = Jwts.parser().verifyWith(signKey).build();
    }

    // 从Token中提取用户名
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 生成Token
    // roles仍是列表，可拓展性
    public String generateAccessToken(UserDetails userDetails) {
        return buildToken(userDetails, expiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(userDetails, refreshExpiration);
    }

    private String buildToken(UserDetails userDetails, long expiration) {
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles",  roles)
                .issuedAt(new Date())
                .expiration(new  Date(System.currentTimeMillis() + expiration))
                .signWith(signKey)
                .compact();
    }

    // 验证Token有效性
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }
}
