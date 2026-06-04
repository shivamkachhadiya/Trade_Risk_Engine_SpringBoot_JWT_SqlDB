package com.shivam.trade_risk_engine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Ye function poore project mein aane wale kisi bhi "RuntimeException" ko pakad lega
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        // Ek clean JSON object banaya
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        errorResponse.put("status", "FAILED");

        // Bad Request (400) status ke sath JSON return kar diya
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}