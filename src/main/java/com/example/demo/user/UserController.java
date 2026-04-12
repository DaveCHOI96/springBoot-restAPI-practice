package com.example.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest request) {
        UserResponse response = userService.saveUser(request);
        // @RequestBody: "손님이 보낸 택배 박스(JSON)를 뜯어서 내용물을 꺼내라!
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

        //response 없이 단일로 사용하면 DB저장과 결과 데이터 반환이 동시에 일어남
        //또 이 데이터를 다른곳에 사용할때 똑같은 User가 DB에 추가적으로 생김
//      return ResponseEntity.status(HttpStatus.CREATED).body(userService.saveUser(request));

//        @PostMapping
//        @ResponseStatus(HttpStatus.CREATED) // 이걸 안 붙이면 무조건 200 OK만 나갑니다.
//        public UserResponse signUp(@RequestBody UserRequest request) {
//            return userService.saveUser(request);
//        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        UserResponse response = userService.getUser(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser
            (@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }


}
