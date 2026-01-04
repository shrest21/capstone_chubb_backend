package com.inventoryapp.authservice.dto;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class LoginServiceResult {
    private Long userId;
    private String token;
    private String name;
    private String email;
    private String role;
    public LoginServiceResult(Long userId,String token, String name, String email, String role) {
        this.userId=userId;
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
