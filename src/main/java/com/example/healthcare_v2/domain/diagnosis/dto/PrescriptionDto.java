package com.example.healthcare_v2.domain.diagnosis.dto;

import com.example.healthcare_v2.domain.prescription.entity.Prescription;

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

    public static PrescriptionDto from(Prescription prescription) {
        return new PrescriptionDto(
                prescription.getId(),
                prescription.getSpecialNotes(),
                prescription.getMedicationName(),
                prescription.getMedicationSchedule(),
                prescription.getDailyDosage(),
                prescription.getTotalDays()
        );
    }

}
