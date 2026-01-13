package com.inventoryapp.authservice.controller;

import com.inventoryapp.authservice.dto.*;
import com.inventoryapp.authservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginServiceResult response = authService.login(request);
        ResponseCookie cookie = ResponseCookie.from("jwt", response.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("Lax")
                .build();
        LoginResponse responseLogin = new LoginResponse();
        responseLogin.setUserId(response.getUserId());
        responseLogin.setName(response.getName());
        responseLogin.setEmail(response.getEmail());
        responseLogin.setRole(response.getRole());
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(responseLogin);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(){
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).body("Logout Succesful");
    }
    @PostMapping("/warehousemanager")
    public ResponseEntity<String> registerWarehouseManager(@Valid @RequestBody RegisterRequest request) {
        authService.registerWarehouseManager(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Warehouse Manager registered successfully");
    }

}
