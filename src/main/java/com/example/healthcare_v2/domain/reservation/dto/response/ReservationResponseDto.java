package com.example.healthcare_v2.domain.reservation.dto.response;

import com.example.healthcare_v2.domain.reservation.dto.DoctorDto;
import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationResponseDto(
        String symptom,
        LocalDate reservationDate,
        LocalTime reservationTime,
        PatientDto patientDto,
        DoctorDto doctorDto) {

    public static ReservationResponseDto from(ReservationDto reservationDto) {
        return new ReservationResponseDto(reservationDto.symptom(),
                reservationDto.reservationDate(),
                reservationDto.reservationTime(),
                reservationDto.patientDto(),
                reservationDto.doctorDto()
        );
    }
}
