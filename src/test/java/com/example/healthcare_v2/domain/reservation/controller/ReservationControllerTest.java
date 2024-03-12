package com.example.healthcare_v2.domain.reservation.controller;

import com.example.healthcare_v2.config.JsonDataEncoder;
import com.example.healthcare_v2.config.TestSecurityConfig;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.reservation.dto.DoctorDto;
import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import com.example.healthcare_v2.domain.reservation.dto.request.ReservationRequestDto;
import com.example.healthcare_v2.domain.reservation.dto.request.ReservationUpdateRequestDto;
import com.example.healthcare_v2.domain.reservation.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("Reservation 컨트롤러 테스트")
@Import({TestSecurityConfig.class, JsonDataEncoder.class})
@WebMvcTest(ReservationController.class)
class ReservationControllerTest {
    @Autowired private JsonDataEncoder jsonDataEncoder;

    @Autowired private MockMvc mvc;

    @MockBean private ReservationService reservationService;


    @WithMockUser(username = "1")
    @DisplayName("예약 전체 조회")
    @Test
    void givenNoting_whenGetReservations_thenReturns200() throws Exception {
        // given
        given(reservationService.getReservations(any(Pageable.class))).willReturn(Page.empty());

        // when & then
        mvc.perform(get("/v2/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").hasJsonPath())
                .andExpect(jsonPath("message").isEmpty()
                );
        then(reservationService).should().getReservations(any(Pageable.class));
    }

    @WithMockUser(username = "1")
    @DisplayName("예약 단건 조회 - 성공")
    @Test
    void givenReservationId_whenGetReservation_thenReturns200() throws Exception {
        // given
        ReservationDto reservationDto = createReservationDto();
        given(reservationService.getReservation(reservationDto.id())).willReturn(createReservationDto());

        // when & then
        mvc.perform(get("/v2/reservations/" + reservationDto.id()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("message").isEmpty()
                );
        then(reservationService).should().getReservation(reservationDto.id());
    }

    @WithMockUser(username = "1")
    @DisplayName("예약 단건 조회 - 실패")
    @Test
    void givenReservationId_whenGetReservation_thenReturns500() throws Exception {
        // given
        Long reservationId = 1L;
        given(reservationService.getReservation(reservationId))
                .willThrow(new EntityNotFoundException("예약이 없습니다. - reservationId: " + reservationId));

        // when & then
        mvc.perform(get("/v2/reservations/" + reservationId))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("message").value("서버 에러!")
                );
        then(reservationService).should().getReservation(reservationId);
    }

    @WithMockUser(username = "1")
    @DisplayName("예약 하기")
    @Test
    void givenReservationInfo_whenCreateNewReservation_thenReturns200() throws Exception {
        // given
        Long patientId = 1L;
        ReservationRequestDto reservationRequestDto = createReservationRequestDto();
        ReservationDto reservationDto = reservationRequestDto.toDto(PatientDto.of(patientId));
        willDoNothing().given(reservationService).saveReservation(reservationDto);

        // when & then
        mvc.perform(post("/v2/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(reservationRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("message").isEmpty()
                );
        then(reservationService).should().saveReservation(reservationDto);
    }

    @WithMockUser(username = "1")
    @DisplayName("예약 변경")
    @Test
    void givenUpdateReservationInfo_whenUpdateReservation_thenReturns200() throws Exception {
        // given
        Long patientId = 1L;
        ReservationUpdateRequestDto reservationUpdateRequestDto = createReservationUpdateRequestDto();
        ReservationDto reservationDto = reservationUpdateRequestDto.toDto(PatientDto.of(patientId));
        willDoNothing().given(reservationService).updateReservation(reservationDto);

        // when & then
        mvc.perform(put("/v2/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonDataEncoder.encode(reservationUpdateRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("message").isEmpty()
                );
        then(reservationService).should().updateReservation(reservationDto);
    }

    @WithMockUser(username = "1")
    @DisplayName("예약 삭제")
    @Test
    void givenReservationIdAndPatientId_whenCancelReservation_thenReturns200() throws Exception {
        // given
        Long reservationId = 1L;
        Long patientId = 1L;
        willDoNothing().given(reservationService).cancelReservation(reservationId, patientId);

        // when & then
        mvc.perform(delete("/v2/reservations/" + reservationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("message").isEmpty()
                );
        then(reservationService).should().cancelReservation(reservationId, patientId);
    }

    private ReservationUpdateRequestDto createReservationUpdateRequestDto() {
        return new ReservationUpdateRequestDto(
                1L,
                "아파 업데이트",
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                1L
        );
    }

    private ReservationRequestDto createReservationRequestDto() {
        return new ReservationRequestDto(
                "증상1",
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                1L);
    }

    private ReservationDto createReservationDto() {
        return ReservationDto.of(
                1L,
                "증상1",
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                PatientDto.from(createPatient()),
                DoctorDto.from(createDoctor())
        );
    }

    private Patient createPatient() {
        Patient patient = Patient.builder()
                .email("환자이메일@email.com")
                .encryptedPassword("password2")
                .phoneNumber("010-1234-5678")
                .build();

        ReflectionTestUtils.setField(patient, "id", 1L);

        return patient;
    }

    private Doctor createDoctor() {
        Doctor doctor = Doctor.builder()
                .email("의사이메일@email.com")
                .encryptedPassword("password1")
                .name("의사이름1")
                .major("의사전공1")
                .build();

        ReflectionTestUtils.setField(doctor, "id", 1L);

        return doctor;
    }


}