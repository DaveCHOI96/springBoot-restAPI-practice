package com.example.demo.water;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WaterSerivce {

    private final WaterRepository waterRepository;
    private final UserRepository userRepository;

    public WaterResponse saveWater(Long userId, WaterRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다 " + userId));
        Water water = Water.builder()
                .amount(request.amount())
                .user(user)
                .build();
        Water savedWater = waterRepository.save(water);
        return WaterResponse.from(savedWater);
    }

    @Transactional(readOnly = true)
    public List<WaterResponse> getWaters(Long userId) {
        List<Water> waters = waterRepository.findByUserId(userId);
        return waters.stream()
                .map(WaterResponse::from)
                .toList();
    }
}
