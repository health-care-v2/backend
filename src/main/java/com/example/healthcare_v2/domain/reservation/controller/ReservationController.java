package com.example.healthcare_v2.domain.reservation.controller;

import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.request.ReservationRequestDto;
import com.example.healthcare_v2.domain.reservation.dto.request.ReservationUpdateRequestDto;
import com.example.healthcare_v2.domain.reservation.dto.response.ReservationResponseDto;
import com.example.healthcare_v2.domain.reservation.service.ReservationService;
import com.example.healthcare_v2.global.utill.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("/v2/reservations")
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    /**
     * 예약 전체 조회
     */
    @GetMapping
    public ResponseEntity<ResponseDTO<Page<ReservationResponseDto>>> reservations(
            @PageableDefault(size = 10, sort = {"reservationDate", "reservationTime"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<ReservationResponseDto> reservations = reservationService.getReservations(pageable).map(ReservationResponseDto::from);
        return ResponseEntity.ok(ResponseDTO.okWithData(reservations));
    }

    /**
     * 예약 상세 조회
     */
    @GetMapping("/{reservationId}")
    public ResponseEntity<ResponseDTO<ReservationResponseDto>> reservation(@PathVariable(name = "reservationId") Long reservationId) {
        ReservationResponseDto reservationResponseDto = ReservationResponseDto.from(reservationService.getReservation(reservationId));
        return ResponseEntity.ok(ResponseDTO.okWithData(reservationResponseDto));
    }

    /**
     * 예약하기
     */
    @PostMapping
    public ResponseEntity<ResponseDTO> createNewReservation(
            @RequestBody ReservationRequestDto reservationRequestDto,
            Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        ReservationResponseDto reservationResponseDto = ReservationResponseDto
                .from(reservationService.saveReservation(reservationRequestDto.toDto(PatientDto.of(userId))));
        return ResponseEntity.ok(ResponseDTO.okWithData(reservationResponseDto));
    }

    /**
     * 예약 변경
     */
    @PutMapping
    public ResponseEntity<ResponseDTO> updateReservation(
            @Valid @RequestBody ReservationUpdateRequestDto reservationUpdateRequestDto,
            Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        ReservationResponseDto reservationResponseDto = ReservationResponseDto
                .from(reservationService.updateReservation(reservationUpdateRequestDto.toDto(PatientDto.of(userId))));
        return ResponseEntity.ok(ResponseDTO.okWithData(reservationResponseDto));
    }

    /**
     * 예약 취소
     */
    @DeleteMapping("/{reservationId}")
    public ResponseEntity<ResponseDTO> deleteReservation(
            @PathVariable(name = "reservationId") Long reservationId,
            Principal principal
    ) {
        Long userId = Long.valueOf(principal.getName());
        reservationService.cancelReservation(reservationId, userId);
        return ResponseEntity.ok(ResponseDTO.ok());
    }

}
