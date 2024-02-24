package com.example.healthcare_v2.domain.patient.controller.response;

import com.example.healthcare_v2.domain.patient.entity.Patient;

public record CreatePatientResponse(
    String email,
    String username
) {

    public static CreatePatientResponse fromEntity(Patient patient) {
        return new CreatePatientResponse(
            patient.getEmail(),
            patient.getName()
        );
    }
}
