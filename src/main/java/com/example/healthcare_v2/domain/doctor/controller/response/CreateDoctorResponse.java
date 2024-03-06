package com.example.healthcare_v2.domain.doctor.controller.response;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;

public record CreateDoctorResponse(
    String email,
    String username
) {

    public static CreateDoctorResponse fromEntity(Doctor doctor) {
        return new CreateDoctorResponse(
            doctor.getEmail(),
            doctor.getName()
        );
    }
}
