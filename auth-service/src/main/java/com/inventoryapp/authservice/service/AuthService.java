package com.inventoryapp.authservice.service;

import com.inventoryapp.authservice.dto.*;

public interface AuthService {
    void register(RegisterRequest request);
    String login(LoginRequest request);
}
