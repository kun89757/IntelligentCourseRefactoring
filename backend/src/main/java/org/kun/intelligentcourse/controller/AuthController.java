package org.kun.intelligentcourse.controller;

import org.kun.intelligentcourse.dto.authDTO.LoginDTO;
import org.kun.intelligentcourse.dto.authDTO.RegisterDTO;
import org.kun.intelligentcourse.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        return authService.login(loginDTO);
    }

    // 前端拦截器处设置accessToken为invalid时向此path发送请求
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken") String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'Teacher')")
    @GetMapping("/dashboard")
    public ResponseEntity<Object> getDashboard() {
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/test")
//    public ResponseEntity<Object> test() {
//        return authService.login(new LoginDTO("Purpurrot", "Aa125516524"));
//    }
}
