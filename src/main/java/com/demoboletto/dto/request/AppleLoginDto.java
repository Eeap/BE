package com.demoboletto.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record AppleLoginDto(
        @NotBlank(message = "identity token은 필수 입력입니다.")
        @JsonProperty("identity_token")
        String identityToken
) {
}
