package com.example.healthcare_v2.domain.diagnosis.dto;

public record PrescriptionDto(
        Long id,
        String specialNotes,
        String medicationName,
        String medicationSchedule,
        Integer dailyDosage,
        Integer totalDays
) {

    public static PrescriptionDto of(Long id) {
        return new PrescriptionDto(id, null, null, null, null, null);
    }
}
