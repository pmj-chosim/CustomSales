package com.example.customsale.controller;

import com.example.customsale.controller.dto.UserCreateRequestDto;
import com.example.customsale.controller.dto.UserResponseDto;
import com.example.customsale.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@Tag(name = "User API", description = "회원(유저) 관련 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {
    UserService userService;

    @Operation(summary = "단일 유저 정보 조회", description = "특정 id를 가진 유저 정보 조회 API")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@NotNull @Min(1) @PathVariable Integer id) {
        UserResponseDto user=userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "전체 유저 정보 조회", description = "전체 유저 정보를 페이지 형태로 조회하는 API (page, size) 지정 가능")
    @GetMapping("")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
             @RequestParam(defaultValue = "0") Integer page,
             @RequestParam(defaultValue = "10") Integer size) {

        Page<UserResponseDto> users=userService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "회원 가입 API", description = "새로운 유저 생성. 아이디 중복 시 가입 불가")
    // 2. 회원가입
    @PostMapping("")
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto){
        UserResponseDto user=userService.createUser(userCreateRequestDto);
        return ResponseEntity.ok(user);
    }


    @Operation(summary = "회원 삭제 API", description = "특정 id를 가진 회원(유저) 정보 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@NotNull @PathVariable Integer id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
