package com.example.healthcare_v2.domain.doctor.controller.request;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDoctorRequest(
    @NotNull(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 유효하지 않습니다.")
    String email,
    @NotNull(message = "비밀 번호를 입력해 주세요.")
    @Size(min = 6, message = "비밀 번호는 6자 이상이어야 합니다.")
    String password,
    @NotNull(message = "사용자 이름을 입력해 주세요.")
    String name,
    String phoneNumber,
    @NotNull(message = "전공을 입력해 주세요.")
    String major
) {

    public Doctor toEntity(String encryptedPassword) {
        return Doctor.builder()
            .email(email)
            .encryptedPassword(encryptedPassword)
            .name(name)
            .phoneNumber(phoneNumber)
            .major(major)
            .build();
    }
}
