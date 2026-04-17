package com.example.demo.user;

import com.example.demo.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;

@Entity
// 팔로우 버튼 연타로, DB 데이터가 2개가 쌓이는 경우 막기
@Table(
        name = "follows",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"follower_id", "following_id"})
   }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Filter(name = "deletedFilter", condition = "is_deleted = false")
@Builder
public class Follow extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    // 연관 관계 편의 메서드
    public void addRelation(User follower, User following) {
        this.follower = follower;
        this.following = following;

        // 유저 객체의 리스트에도 '나(this)'를 추가해서 동기화
        follower.getFollowings().add(this);
        following.getFollowers().add(this);
    }
}
