package com.example.demo.meal;

import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "meals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;
    private Integer calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //DB 외래키(FK) 이름
    private User user;
}
