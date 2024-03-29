package com.example.healthcare_v2.domain.reservation.dto;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDto(Long id,
                             String symptom,
                             LocalDate reservationDate,
                             LocalTime reservationTime,
                             Long patientId,
                             Long doctorId) {

    public static ReservationDto of(String symptom,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    Long patientId,
                                    Long doctorId) {
        return new ReservationDto(null, symptom, reservationDate, reservationTime, patientId, doctorId);
    }

    public static ReservationDto of(Long id,
                                    String symptom,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    Long patientId,
                                    Long doctorId) {
        return new ReservationDto(id, symptom, reservationDate, reservationTime, patientId, doctorId);
    }

    public Reservation toEntity(Patient patient, Doctor doctor) {
        return Reservation.of(
                symptom,
                reservationDate,
                reservationTime,
                doctor,
                patient
        );
    }

    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.of(reservation.getSymptom(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                reservation.getDoctor().getId(),
                reservation.getPatient().getId()
        );
    }
}
