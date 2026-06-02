package com.shivam.trade_risk_engine.service;

import com.shivam.trade_risk_engine.config.JwtUtil;
import com.shivam.trade_risk_engine.dto.LoginRequest;
import com.shivam.trade_risk_engine.dto.RegisterRequest;
import com.shivam.trade_risk_engine.model.User;
import com.shivam.trade_risk_engine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request){
        User user=new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());
        user.setAvailableBalance(request.getAvailableBalance());
        userRepository.save(user);
        return "User Registered Successfully....";
    }

    public String login(LoginRequest request){
        Optional<User>userOpt=userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()){
            throw new RuntimeException("User Not Found");
        }
        User user=userOpt.get();
        if(!passwordEncoder.matches(request.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid Password");
        }
        return jwtUtil.generateToken(user.getEmail());
    }
}
