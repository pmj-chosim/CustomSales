package com.example.customsale.service;

import com.example.customsale.controller.dto.UserCreateRequestDto;
import com.example.customsale.controller.dto.UserResponseDto;
import com.example.customsale.repository.UserRepository;
import com.example.customsale.repository.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository uR;

    public UserResponseDto getUserById(Integer id) {
        User user=uR.findById(id).orElseThrow(()->new RuntimeException("User id"+id+"not found"));
        return UserResponseDto.from(user);
    }

    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = uR.findAll(pageable);
        return userPage.map(UserResponseDto::from);
    }

    public UserResponseDto createUser(UserCreateRequestDto userCreateRequestDto) {
        User user=User.create(
                userCreateRequestDto.getName(),
                userCreateRequestDto.getPwd_hash(),
                userCreateRequestDto.getEmail(),
                userCreateRequestDto.getPhoneNumber()
        );
        User savedUser=uR.save(user);
        return UserResponseDto.from(savedUser);
    }

    public void deleteUser(Integer id){
        uR.deleteById(id);
    }
}
