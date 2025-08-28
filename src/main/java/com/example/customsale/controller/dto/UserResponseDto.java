package com.example.customsale.controller.dto;


import com.example.customsale.repository.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponseDto {
    private Integer id;
    private String name;
    private String email;
    private  String phoneNumber;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
          user.getId(), user.getName(), user.getEmail(), user.getPhoneNumber()
        );
    }

}
