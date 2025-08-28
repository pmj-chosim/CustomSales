package com.example.customsale.service;

import com.example.customsale.controller.dto.UserCreateRequestDto;
import com.example.customsale.controller.dto.UserResponseDto;
import com.example.customsale.repository.UserRepository;
import com.example.customsale.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder; // PasswordEncoder를 사용하기 위해 import

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository uR; //풀네임 적자
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    public UserResponseDto getUserById(Integer id) {
        User user = uR.findById(id).orElseThrow(() -> new RuntimeException("User id" + id + "not found"));
        return UserResponseDto.from(user);
    }

    public UserResponseDto getUserByName(String username) {
        User user=uR.findByName(username).orElseThrow(()->new RuntimeException(username+"<-username 존재 x"));
        return UserResponseDto.from(user);
    }

    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = uR.findAll(pageable);
        return userPage.map(UserResponseDto::from);
    }

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {
        // 1. 아이디 중복 검증 (if xxxx -> 함수형)
        // findByName 결과가 존재하면 예외 발생시
        uR.findByName(userCreateRequestDto.getName())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 사용자 아이디입니다.");
                });


        // 2. 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(userCreateRequestDto.getPwd());

        // 3. User 엔티티 생성 및 저장
        User user = User.create(
                userCreateRequestDto.getName(),
                encodedPassword,
                userCreateRequestDto.getEmail(),
                userCreateRequestDto.getPhoneNumber()
        );
        User savedUser = uR.save(user);

        return UserResponseDto.from(savedUser);
    }

    public void deleteUser(Integer id) {
        //존재 여부 확인
        User user= uR.findById(id).orElseThrow(()->new RuntimeException("userid"+id+"가 존재하지 않음"));
        uR.deleteById(id);
    }

    // 이 메서드를 추가합니다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 데이터베이스에서 사용자를 찾습니다.
        User user = uR.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자 " + username + "을 찾을 수 없습니다."));

        // 2. Spring Security가 이해할 수 있는 UserDetails 객체로 반환합니다.
        // 이때, 비밀번호는 반드시 해시된 값(pwd_hash)을 사용해야 합니다.
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPwd_hash(),
                Collections.emptyList()
        );
    }


    public UserResponseDto getUserIdByName(String username) {
        User user=uR.findByName(username).orElseThrow(()->new RuntimeException(username+"<-username 존재 x"));
        return UserResponseDto.from(user);
    }
}
