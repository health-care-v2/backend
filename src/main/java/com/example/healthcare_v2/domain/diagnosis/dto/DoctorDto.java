package com.example.healthcare_v2.domain.diagnosis.dto;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;

public record DoctorDto(
        Long id,
        String email,
        String name,
        String major,
        String phoneNumber,
        String code
) {

    public static DoctorDto from(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getEmail(),
                doctor.getName(),
                doctor.getMajor(),
                doctor.getPhoneNumber(),
                doctor.getCode()
        );
    }

    public static DoctorDto from(Long doctorId) {
        return new DoctorDto(
                doctorId,
                null,
                null,
                null,
                null,
                null);
    }
}
