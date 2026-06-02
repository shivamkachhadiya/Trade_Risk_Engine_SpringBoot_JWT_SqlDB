package com.shivam.trade_risk_engine.repository;

import com.shivam.trade_risk_engine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
}

//
//Samajh — 3 cheezein:
//JpaRepository<User, Long> — Spring ka ready-made repository hai. Isme pehle se save(),
//findAll(), findById() sab bana hua hai. Tu likhega nahi — automatically milta hai.
//        Optional<User> — user mile ya na mile dono handle karta hai. Agar email DB mein nahi hai
//        — Optional.empty() aayega, NullPointerException nahi.
//findByEmail(String email) — tu sirf method ka naam likha — Spring automatically SQL query bana deta hai:
//sqlSELECT * FROM users WHERE email = ?
//Tu SQL nahi likha — method naam se Spring samajh gaya.