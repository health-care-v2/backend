package com.example.healthcare_v2.domain.patient.controller.request;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
    @NotNull(message = "email은 필수값입니다.")
    String email,

    @NotNull(message = "password는 필수값입니다.")
    String password
) {

}
