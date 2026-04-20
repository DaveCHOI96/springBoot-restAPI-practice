package com.example.demo.user;

import com.example.demo.common.BaseEntity;
import com.example.demo.meal.Meal;
import com.example.demo.water.Water;
import com.example.demo.workout.Workout;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Filter(name = "deletedFilter", condition = "is_deleted = false")
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    //defaultValue
    //int = 0, Integer = null
    @Column(nullable = false, length = 5)
    private Integer age;

    @Column(nullable = false, unique = true, length = 50)
    private String phoneNumber;

    @ColumnDefault("2000")
    private Integer targetKcal;

    @Column(nullable = false)
    private Integer targetWorkoutDuration = 60;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Workout> workouts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<Water> waters = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.ALL)
    public List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.ALL)
    public List<Follow> followings = new ArrayList<>();



    //@NoArgsConstructor(access = AccessLevel.PROTECTED)
    //protected  User() {}

    //@AllArgsConstructor
    //public User(String name, String email) {
        //this.name = name;
        //this.email = email;
    //}

    //Getter
//    public Long getId() { return id; }
//    public String getName() { return name; }
//    public String getEmail() { return email; }

    //JPA(실무 도구)에서는 DB를 직접 건드리지 않고, **자바 객체의 값만 바꾸면 DB가 알아서 바뀌는
    // '더티 체킹(Dirty Checking)'**이라는 마법을 씁니다. 그러려면 엔티티에 수정 메서드가 있어야 해요.
    //왜 여기인가? 자신의 정보를 바꾸는 책임은 그 데이터를 가진 엔티티(User) 본인에게 있기 때문
    public void update(String name, Integer age, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public void updateTargetDuration(Integer newTarget) {
        if (newTarget <= 0) throw new IllegalArgumentException("목표 시간은 0보다 커야합니다.");
        this.targetWorkoutDuration = newTarget;
    }

    public void softDeletes() {
        // 1. 본인(User) 삭제 상태 변경 (BaseEntity의 메서드 활용)
        super.softDelete();

        // 2. 자식들 연쇄 softDelete
        this.workouts.forEach(Workout::softDelete);
        this.meals.forEach(Meal::softDelete);
        this.followers.forEach(Follow::softDelete);
        this.followings.forEach(Follow::softDelete);

    }

    public void restores() {
        super.restore();

        this.workouts.forEach(Workout::restore);
        this.meals.forEach(Meal::restore);
        this.followers.forEach(Follow::restore);
        this.followings.forEach(Follow::restore);
    }
}



