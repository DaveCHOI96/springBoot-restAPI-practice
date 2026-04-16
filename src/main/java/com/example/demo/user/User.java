package com.example.demo.user;

import com.example.demo.meal.Meal;
import com.example.demo.water.Water;
import com.example.demo.workout.Workout;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SQLRestriction("is_deleted = false") // 모든 SELECT 쿼리에 이 SQL 조건이 강제로 붙습니다.
@Builder
public class User {

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

    private boolean isDeleted = false;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Workout> workouts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Meal> meals = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<Water> waters = new ArrayList<>();

    @OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    public void softDelete() {
        if (this.isDeleted) {
            throw new IllegalArgumentException("이미 삭제된 사용자 입니다.");
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        if (!this.isDeleted) {
            throw new IllegalArgumentException("삭제되지 않은 사용자는 복구할 수 없습니다.");
        }
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
