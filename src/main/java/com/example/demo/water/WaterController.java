package com.example.demo.water;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WaterController {

    // 여기에 final이 꼭 붙어야 @RequiredArgsConstructor가 생성자를 만들어줍니다!
    private final WaterSerivce waterSerivce;

    @PostMapping("/users/{userId}/waters")
    public ResponseEntity<WaterResponse> addWater(
            //@Valid <- 해당 Repository 검증 필요
            @PathVariable Long userId, @Valid @RequestBody WaterRequest request) {
        WaterResponse response = waterSerivce.saveWater(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}/waters")
    public ResponseEntity<List<WaterResponse>> getWater(@PathVariable Long userId) {
        List<WaterResponse> response = waterSerivce.getWaters(userId);
        return ResponseEntity.ok(response);
    }
}
