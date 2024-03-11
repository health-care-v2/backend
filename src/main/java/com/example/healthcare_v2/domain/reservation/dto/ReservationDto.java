package com.example.healthcare_v2.domain.reservation.dto;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDto(
        Long id,
        String symptom,
        LocalDate reservationDate,
        LocalTime reservationTime,
        PatientDto patientDto,
        DoctorDto doctorDto) {

    public static ReservationDto of(String symptom,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    PatientDto patientDto,
                                    DoctorDto doctorDto) {
        return new ReservationDto(null, symptom, reservationDate, reservationTime, patientDto, doctorDto);
    }

    public static ReservationDto of(Long id,
                                    String symptom,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    PatientDto patientDto,
                                    DoctorDto doctorDto) {
        return new ReservationDto(id, symptom, reservationDate, reservationTime, patientDto, doctorDto);
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
        return ReservationDto.of(
                reservation.getId(),
                reservation.getSymptom(),
                reservation.getReservationDate(),
                reservation.getReservationTime(),
                PatientDto.from(reservation.getPatient()),
                DoctorDto.from(reservation.getDoctor())
        );
    }
}
