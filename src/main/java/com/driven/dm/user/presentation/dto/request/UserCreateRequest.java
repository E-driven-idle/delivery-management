package com.driven.dm.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserCreateRequest(
    @NotBlank(message = "ID는 필수입니다.")
    @Size(min = 2, max = 10, message = "ID는 2~10자여야 합니다.")
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
        message = "닉네임은 한글/영문/숫자만 사용할 수 있습니다."
    )
    String username,

    @NotBlank(message = "password는 필수입니다.")
    @Pattern(
        regexp = "^(?=.{10,64}$)(?!.*\\s)(?:(?=.*[A-Z])(?=.*[a-z])|(?=.*[A-Z])(?=.*\\d)|(?=.*[a-z])(?=.*\\d)).*$",
        message = "비밀번호는 길이 10~64자이며, 대문자/소문자/숫자 중 2가지 이상을 포함해야 합니다."
    )
    String password,

    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(min = 2, max = 10, message = "닉네임은 2~10자여야 합니다.")
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9_-]{2,10}$",
        message = "닉네임은 한글/영문/숫자/언더스코어(_)/하이픈(-)만 사용할 수 있습니다."
    )
    String nickname
) {

}
