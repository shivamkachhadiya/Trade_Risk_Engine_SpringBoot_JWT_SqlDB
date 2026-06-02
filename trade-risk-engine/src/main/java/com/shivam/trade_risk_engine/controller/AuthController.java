package com.shivam.trade_risk_engine.controller;


import com.shivam.trade_risk_engine.dto.LoginRequest;
import com.shivam.trade_risk_engine.dto.RegisterRequest;
import com.shivam.trade_risk_engine.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String>register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<String>login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

}
