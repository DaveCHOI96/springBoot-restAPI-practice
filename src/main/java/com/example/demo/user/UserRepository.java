package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    // [중요] Jpa 기본 메서드는 Filter를 무시하고 읽어들임, 즉 Filter 사용할려면 쿼리 메서드를 따로 만들어야함

    boolean existsByEmail(String email);

    @Query("select u from User u where u.id = :id")
    Optional<User> findActiveUserById(@Param("id") Long id);

    //엔티티에 붙은 @SQLRestriction은 JPA/Hibernate가 쿼리를 가공할 때 끼어드는 것
    //nativeQuery = true 는 JPA를 거치지않고 DB에 전달됨
//    @Query(value = "SELECT * FROM users WHERE id = :id", nativeQuery = true)
//    Optional<User> findByIdIncludingDeleted(@Param("id") Long id);

    // 1. 대상자 조회를 위한 메서드
    List<User> findAllByIsDeletedTrueAndDeletedAtBefore(LocalDateTime threshold);

    // 2. 삭제를 위한 메서드 (ID 리스트로 한 번에 삭제)
    void deleteAllByIdIn(List<Long> ids);
}
