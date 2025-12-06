package com.example.apiserver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "Health Check", description = "서버 상태 확인 API")
@RestController
@RequestMapping("/actuator")
public class HealthController {
    
    @Operation(summary = "Health Check", description = "서버 상태를 확인합니다.")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("service", "digilog");
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Info", description = "서버 정보를 조회합니다.")
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "Digilog API Server");
        response.put("version", "1.0.0");
        response.put("description", "Digilog API 서버");
        return ResponseEntity.ok(response);
    }
}

