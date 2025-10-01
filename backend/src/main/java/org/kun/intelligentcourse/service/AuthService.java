package org.kun.intelligentcourse.service;

import org.kun.intelligentcourse.dto.authDTO.JWTResponseDTO;
import org.kun.intelligentcourse.dto.authDTO.LoginDTO;
import org.kun.intelligentcourse.dto.authDTO.RegisterDTO;
import org.kun.intelligentcourse.entity.Role;
import org.kun.intelligentcourse.entity.User;
import org.kun.intelligentcourse.exception.ConflictException;
import org.kun.intelligentcourse.exception.InvalidTokenException;
import org.kun.intelligentcourse.exception.NotFoundException;
import org.kun.intelligentcourse.repository.RoleRepo;
import org.kun.intelligentcourse.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepo  userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;

    public ResponseEntity<Object> login(LoginDTO loginDTO) {
        JWTResponseDTO jwtToken = authenticate(loginDTO);
        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", jwtToken.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMillis(jwtService.getExpiration()))
                .sameSite("Lax")
                .build();
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", jwtToken.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/auth/refreshToken")
                .maxAge(Duration.ofMillis(jwtService.getRefreshExpiration()))
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        accessTokenCookie.toString(),
                        refreshTokenCookie.toString())
                .build();
    }

    public ResponseEntity<Object> register(RegisterDTO registerDTO) {
        if  (userRepo.findByUsername(registerDTO.getUsername()).isPresent()) {
            throw new ConflictException("Username is already taken!");
        }
        if (userRepo.findByEmail(registerDTO.getEmail()).isPresent()) {
            throw new ConflictException("Email is already taken!");
        }
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        Role role = roleRepo.findByName("ROLE_USER")
                .orElseThrow(() -> new NotFoundException("Role is not found!"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        user.setBlocked(false);
        userRepo.save(user);
        return ResponseEntity.ok().build();
    }

    private JWTResponseDTO authenticate(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepo.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("User is not found!"));
        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpireDate(OffsetDateTime.now().plusSeconds(jwtService.getRefreshExpiration() / 1000));
        userRepo.save(user);
        return new JWTResponseDTO(accessToken, refreshToken);
    }

    public ResponseEntity<Object> refreshToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new InvalidTokenException("Refresh token is invalid!");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        User user =  userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User is not found!"));
        if (!jwtService.isTokenValid(refreshToken, userDetails) || !user.getRefreshTokenExpireDate().isBefore(OffsetDateTime.now())) {
            throw new InvalidTokenException("Refresh token is expired or invalid!");
        }
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        ResponseCookie newAccessTokenCookie = ResponseCookie.from("accessToken", newAccessToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofMillis(jwtService.getExpiration()))
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
                .build();
    }
}
