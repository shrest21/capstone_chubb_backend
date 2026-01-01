package com.inventoryapp.authservice.dto;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginServiceResult {
    private String token;
    private String name;
    private String email;
    private String role;
    public LoginServiceResult(String token, String name, String email, String role) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
