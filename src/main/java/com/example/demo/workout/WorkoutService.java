package com.example.demo.workout;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final UserRepository userRepository;

    @CacheEvict(value = "todaySummary", key = "#userId")
    public WorkoutResponse savaWorkout(Long userId, WorkoutRequest request) {
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
        // 객체 생성 조립 설명서 builder ~ build
        Workout workout = Workout.builder()
                .title(request.title())
                .duration(request.duration())
                //위에서 찾은 실제 유저 객체를 운동 데이터에 쏙 집어넣어
                // "이 운동의 주인은 이 사람이다"라고 연결(Mapping)
                .user(user)
                .build();

        // 연관 관계 편의 메서드 사용
        // workout -> user 설정과 user -> workout 리스트 추가가 동시에 완료
        workout.confirmUser(user);

        //void가 아닌 WorkoutResponse를 사용할 경우 return 반환이 필요하기에
        // workoutRepository.save(workout); DB에 workout 엔티티를 영구저장
        Workout savadWorkout = workoutRepository.save(workout);
        //DB용 복잡한 데이터(Entity)를 다시 화면용 깔끔한 데이터(Response)로
        //변환(from)해서 문지기(Controller)에게 전달합니다.
        return WorkoutResponse.from(savadWorkout);
    }

    @Transactional(readOnly = true)
    public List<WorkoutResponse> getWorkouts(Long userId) {
        List<Workout> workouts = workoutRepository.findByUserId(userId);

        //리스트에 담긴 운동들을 하나씩 꺼내기 위해 **"컨베이어 벨트"**에 올리는 작업
        return workouts.stream()
                //컨베이어 벨트를 지나가는 운동 엔티티(Workout) 하나하나를 붙잡아서
                //영수증(WorkoutResponse)으로 포장지를 갈아 끼우는(Mapping) 작업
                .map(WorkoutResponse::from)
                //포장이 끝난 영수증들을 다시 차곡차곡 모아서 **새로운 리스트(바구니)**에 담습니다.
                .toList();
    }

    public WorkoutResponse updateWorkout(Long userId, Long workoutId, WorkoutRequest request) {
        Workout workout = workoutRepository.findActiveWorkoutById(workoutId)
                .orElseThrow(() -> new IllegalArgumentException("해당 운동 기록이 존재하지 않습니다."));

        if (!workout.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 운동기록을 수정할 수 있는 권한이 없습니다.");
        }
        workout.update(request.title(), request.duration());
        return WorkoutResponse.from(workout);
    }

    @Transactional(readOnly = true)
    public WorkoutSummary getWorkoutSummary(Long userId) {
        User user = userRepository.findActiveUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        //오늘 운동 시간 합산 (DB SUM 쿼리 방식 호출)
        Integer totalDuration = getTodayTotalDuration(userId);

        //DTO로 변환하여 리턴 (achievementRate 계산 로직은 record 내부의 of 메서드에 있음)
        return WorkoutSummary.of(user, totalDuration);
    }

    @Transactional(readOnly = true)
    public Integer getTodayTotalDuration(Long userId) {
        LocalDateTime startOfDay = LocalDate.now(ZoneId.of("Asia/Seoul")).atStartOfDay();
        Integer total = workoutRepository.calculateTotalDurationToday(userId, startOfDay);

        // 기록이 없으면 null이 오므로 0을 반환
        return total != null ? total : 0;
    }
}
