package com.example.healthcare_v2.domain.diagnosis.dto;

import com.example.healthcare_v2.domain.patient.entity.Patient;

import java.time.LocalDate;

public record PatientDto(
        Long id,
        String email,
        String name,
        LocalDate born,
        String phoneNumber,
        String addr1,
        String addr2
) {

    public static PatientDto from(Patient patient) {
        return new PatientDto(
                patient.getId(),
                patient.getEmail(),
                patient.getName(),
                patient.getBorn(),
                patient.getPhoneNumber(),
                patient.getAddr1(),
                patient.getAddr2()
        );
    }

    public static PatientDto from(Long patientId) {
        return new PatientDto(
                patientId,
                null,
                null,
                null,
                null,
                null,
                null);
    }
}
