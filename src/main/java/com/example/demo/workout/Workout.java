package com.example.demo.workout;

import com.example.demo.common.BaseEntity;
import com.example.demo.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

@Entity
@Table(name = "workouts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Filter(name = "deletedFilter", condition = "is_deleted = false")
@Builder
public class Workout extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    private Integer duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 편의 메서드 추가: workout.setUser(user)를 호출하면 user의 리스트에도 추가되게!
    public void confirmUser(User user) {
          // workout데이터를 다른 user에게 옮기는 기능을 추가 했을때 안전을 위해 사용
          // 중복 피하기 -> 기존에 이미 연결된 유저가 있었다면, 그 유저의 리스트에서 workout 제거
//        if (this.user != null) {
//            this.user.getWorkouts().remove(this);
//        }
        this.user = user;
        if (user != null && !user.getWorkouts().contains(this)) {
            user.getWorkouts().add(this);
        }
    }

}
