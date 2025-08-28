package com.example.customsale.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.*;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserCreateRequestDto {
    @NotNull(message = "아이디는 필수입니다.")
    @Size(min = 5, max = 10, message = "아이디는 5자 이상 10자 이하여야 합니다.")
    private String name;
    // 비밀번호는 최소 8자, 대문자/소문자 각각 1개 이상 포함
    @NotNull(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z]).*$",
            message = "비밀번호는 대문자와 소문자를 각각 1개 이상 포함해야 합니다.")
    private String pwd;
    private String email;
    private String phoneNumber;
}
