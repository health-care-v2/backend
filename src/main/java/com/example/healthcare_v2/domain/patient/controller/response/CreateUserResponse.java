package com.example.healthcare_v2.domain.patient.controller.response;

import com.example.healthcare_v2.domain.patient.entity.Patient;

public record CreateUserResponse(
    String email,
    String username
) {

    public static CreateUserResponse fromEntity(Patient patient) {
        return new CreateUserResponse(
            patient.getEmail(),
            patient.getName()
        );
    }
}
