package com.example.healthcare_v2.domain.reservation.dto.request;

import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationRequestDto(String symptom,
                                    LocalDate reservationDate,
                                    LocalTime reservationTime,
                                    Long patientId,
                                    Long doctorId) {

    public ReservationDto toDto() {
        return ReservationDto.of(symptom, reservationDate, reservationTime, patientId, doctorId);
    }
}
