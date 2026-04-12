package com.example.demo.water;

import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WaterSerivce {

    private final WaterRepository waterRepository;
    private final UserRepository userRepository;

    public
}
