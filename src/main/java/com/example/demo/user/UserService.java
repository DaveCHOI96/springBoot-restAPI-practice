package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor // repository 연결을 자동으로 해줌
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserResponse saveUser(UserRequest request) {
        // [1단계: 창고에 물어보기] "이 이메일 가진 사람 있어?"
        if (userRepository.existsByEmail(request.email())) {
            // [2단계: 거절하기] 있다면 여기서 작업을 멈추고 에러를 던집니다.
            throw new IllegalArgumentException("이미 가입된 이메일입니다." + request.email());
        }
         User user= User.builder()
                 .name(request.name())
                 .email(request.email())
                 .age(request.age())
                 .phoneNumber(request.phoneNumber())
                 .build();

        // [창고 저장] 조립된 유저를 창고(DB)에 넣습니다.
         User savedUser = userRepository.save(user);

        // [영수증 발행] 저장된 유저 정보를 영수증(Response)에 담아 돌려줍니다.
         return UserResponse.from(savedUser);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long id) {
        User user = userRepository.findById(id)
                //박스를 까는 도구(orElseThrow) 없이는 알맹이 변수(User user)에 데이터를 넣는 것 자체가 불가능
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));
        return UserResponse.from(user);
    }

    //@Transactional의 마법: 이 어노테이션이 붙어 있으면, 메서드가 끝날 때
    // 스프링이 "어? user 객체 값이 바뀌었네?" 하고 알아채서 자동으로 DB에
    // UPDATE 쿼리를 날려줍니다. (우리가 save를 또 호출할 필요가 없어요!)
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 유저가 없습니다"));

        user.update(request.name(), request.age(), request.phoneNumber());
        return UserResponse.from(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        user.softDelete();
    }

    public UserRestoreResponse restoreUser(Long id) {
        User user = userRepository.findByIdIncludingDeleted(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        user.restore();
    }

}
