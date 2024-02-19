package com.example.healthcare_v2.domain.reservation.dto;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDto(String symptom,
                             LocalDate reservationDate,
                             LocalTime reservationTime,
                             Long patientId,
                             Long doctorId) {

    public static ReservationDto of(String symptom,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    Long patientId,
                                    Long doctorId) {
        return new ReservationDto(symptom, reservationDate, reservationTime, patientId, doctorId);
    }

    public Reservation toEntity(ReservationDto reservationDto, Patient patient, Doctor doctor) {
        return Reservation.of(
                symptom,
                reservationDate,
                reservationTime,
                doctor,
                patient
        );
    }
}
