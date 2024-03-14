package com.example.healthcare_v2.domain.reservation.dto.request;

import com.example.healthcare_v2.domain.reservation.dto.DoctorDto;
import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationUpdateRequestDto(
        @NotNull String symptom,
        @NotNull LocalDate reservationDate,
        @NotNull LocalTime reservationTime,
        @NotNull Long doctorId) {
    public ReservationDto toDto(Long ReservationId, PatientDto patientDto) {
        return ReservationDto.of(ReservationId, symptom, reservationDate, reservationTime, patientDto, DoctorDto.of(doctorId));
    }
}
