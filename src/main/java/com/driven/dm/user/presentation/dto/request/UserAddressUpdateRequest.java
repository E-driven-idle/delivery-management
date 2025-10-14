package com.driven.dm.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserAddressUpdateRequest(

    @NotBlank(message = "우편번호는 필수입니다.")
    @Pattern(regexp = "\\d{5}", message = "우편번호는 숫자 5자리여야 합니다.")
    String zipCode,

    @NotBlank(message = "기본 주소는 필수입니다.")
    @Size(max = 255, message = "기본 주소는 255자 이하여야 합니다.")
    String primaryAddress,

    @Size(max = 255, message = "상세 주소는 255자 이하여야 합니다.")
    String detailAddress,

    Boolean isDefault
) {}
