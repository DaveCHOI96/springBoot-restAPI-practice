package com.example.demo.user;

import jakarta.persistence.*;
import lombok.*;

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
@Builder
public class Follow {

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
    public void setFollower(User follower) {
        this.follower = follower;
        follower.getFollowings().add(this); // User 엔티티의 팔로잉 리스트에 추가
    }

    public void setFollowing(User following) {
        this.following = following;
        following.getFollowers().add(this); // User 엔티티의 팔로워 리스트에 추가
    }
}
