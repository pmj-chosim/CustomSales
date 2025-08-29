package com.example.customsale.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import jakarta.validation.constraints.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "유저 회원가입 요청 DTO")
public class UserCreateRequestDto {

    @Schema(description = "유저 이름", example="testuser")
    @NotNull(message = "이름은 필수입니다.")
    @Size(min = 5, max = 10, message = "이름은 5자 이상 10자 이하여야 합니다.")
    private String name;

    @Schema(description = "유저 비밀번호",example="Abcd12345" )
    // 비밀번호는 최소 8자, 대문자/소문자 각각 1개 이상 포함
    @NotNull(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z]).*$",
            message = "비밀번호는 대문자와 소문자를 각각 1개 이상 포함해야 합니다.")
    private String pwd;

    @Schema(description = "유저 이메일", example="pppp@mmm.com")
    private String email;
    @Schema(description = "유저 폰번호", example = "010-1111-2222")
    private String phoneNumber;
}
