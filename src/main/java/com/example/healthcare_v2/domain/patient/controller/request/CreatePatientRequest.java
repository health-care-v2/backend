package com.example.healthcare_v2.domain.patient.controller.request;

import com.example.healthcare_v2.domain.patient.entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreatePatientRequest(
    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 유효하지 않습니다.")
    String email,
    @NotNull(message = "비밀 번호를 입력해 주세요.")
    @Size(min = 6, message = "비밀 번호는 6자 이상이어야 합니다.")
    String password,
    @NotNull(message = "사용자 이름을 입력해 주세요.")
    String name,
    String phoneNumber,
    String addr1,
    String addr2
) {

    public Patient toEntity(String encryptedPassword) {
        return Patient.builder()
            .email(email)
            .encryptedPassword(encryptedPassword)
            .name(name)
            .phoneNumber(phoneNumber)
            .addr1(addr1)
            .addr2(addr2)
            .build();
    }
}
