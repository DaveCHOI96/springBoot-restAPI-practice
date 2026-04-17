package com.example.demo.meal;

import com.example.demo.common.BaseEntity;
import com.example.demo.common.BaseTimeEntity;
import com.example.demo.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "meals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Filter(name = "deletedFilter", condition = "is_deleted = false")
@Builder
public class Meal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String foodName;
    private Integer calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") //DB 외래키(FK) 이름
    private User user;

    public void update(String foodName, Integer calories) {
        this.foodName = foodName;
        this.calories = calories;
    }

    public void confirmUser(User user) {
        if (this.user != null) {
            this.user.getMeals().remove(this);
        }
        this.user = user;

        // user.getMeals().contains(this) 체크
        // List의 contains는 리스트를 처음부터 끝까지 훑기
        // but 데이터 많아지면 성능 잡아먹음
        if (user != null && !user.getMeals().contains(this)) {
            user.getMeals().add(this);
        }
    }
}
