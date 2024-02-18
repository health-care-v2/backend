package com.example.healthcare_v2.domain.reservation.controller;

import com.example.healthcare_v2.domain.reservation.dto.request.ReservationRequestDto;
import com.example.healthcare_v2.domain.reservation.service.ReservationService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/v2/reservations")
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ResponseDTO> createNewReservation(@RequestBody ReservationRequestDto reservationRequestDto) {
        reservationService.saveReservation(reservationRequestDto.toDto(reservationRequestDto));
        return ResponseEntity.ok(ResponseDTO.ok());
    }

}
