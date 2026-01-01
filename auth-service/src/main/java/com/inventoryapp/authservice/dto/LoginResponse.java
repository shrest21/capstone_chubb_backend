package com.inventoryapp.authservice.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private String name;
    private String email;
    private String role;
    public LoginResponse(String name, String email, String role) {
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
