package com.example.doan.Security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encoderPassword = encoder.encode(rawPassword);

        System.out.println(encoderPassword);
    }
}
