package com.example.healthcare_v2.domain.reservation.dto.request;

import com.example.healthcare_v2.domain.reservation.dto.DoctorDto;
import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequestDto(
        String symptom,
        LocalDate reservationDate,
        LocalTime reservationTime,
        Long doctorId) {

    public ReservationDto toDto(PatientDto patientDto) {
        return ReservationDto.of(symptom, reservationDate, reservationTime, patientDto, DoctorDto.of(doctorId));
    }
}
