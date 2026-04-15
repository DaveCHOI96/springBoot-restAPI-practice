package com.example.demo.water;

public record WaterResponse(
        Long id,
        Integer amount
) {
    public static WaterResponse from(Water water) {
        return new WaterResponse(water.getId(), water.getAmount());
    }
}
