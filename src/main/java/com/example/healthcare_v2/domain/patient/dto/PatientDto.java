package com.example.healthcare_v2.domain.patient.dto;

import com.example.healthcare_v2.domain.patient.entity.Patient;

import java.time.LocalDate;

public record PatientDto(
        String email,
        String name,

        LocalDate born,

        String phoneNumber,

        String addr1,

        String addr2
) {

    public static PatientDto fromEntity(Patient patient) {
        return new PatientDto(
                patient.getEmail(),
                patient.getName(),
                patient.getBorn(),
                patient.getPhoneNumber(),
                patient.getAddr1(),
                patient.getAddr2()
        );
    }
}
