package com.example.healthcare_v2.domain.reservation.service;


import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import com.example.healthcare_v2.domain.reservation.dto.DoctorDto;
import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import com.example.healthcare_v2.domain.reservation.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("ReservationService - 테스트")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    public static final String RESERVATION_NOT_FOUND_MSG = "예약이 없습니다. - reservationId: ";
    @InjectMocks private ReservationService sut;
    @Mock private ReservationRepository reservationRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;

    @DisplayName("예약정보를 입력하면, 예약이 완료된다.")
    @Test
    void givenReservationInfo_whenSavingReservation_thenSavedReservation() {
        // Given
        ReservationDto dto = createReservationDto();
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        given(patientRepository.getReferenceById(dto.patientDto().id())).willReturn(patient);
        given(doctorRepository.getReferenceById(dto.doctorDto().id())).willReturn(doctor);

        // When
        sut.saveReservation(dto);

        // Then
        then(patientRepository).should().getReferenceById(dto.patientDto().id());
        then(doctorRepository).should().getReferenceById(dto.doctorDto().id());
        then(reservationRepository).should().save(any(Reservation.class));
    }

    @DisplayName("모든 예약을 조회하면, 페이징된 예약정보들을 반환한다.")
    @Test
    void givenPageInfo_whenSearchingReservations_thenReservations() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(reservationRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<ReservationDto> reservations = sut.getReservations(pageable);

        // Then
        assertThat(reservations).isEqualTo(Page.empty());
        then(reservationRepository).should().findAll(pageable);
    }

    @DisplayName("예약 id로 조회하면, 예약 정보를 반환한다.")
    @Test
    void givenReservationId_whenSearchingReservation_thenReturnReservationInfo() {
        // Given
        Long reservationId = 1L;
        Reservation reservation = createReservation();
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(reservation));

        // When
        ReservationDto reservationDto = sut.getReservation(reservationId);

        // Then
        assertThat(reservationDto.id()).isEqualTo(reservationId);
        then(reservationRepository).should().findById(reservationId);
    }

    @DisplayName("없는 예약 id로 조회하면, 예외가 발생한다.")
    @Test
    void givenReservationId_whenGetReservation_thenThrowsException() {
        // Given
        Long reservationId = 1L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            sut.getReservation(reservationId);
        });

        assertThat(exception.getMessage()).isEqualTo(RESERVATION_NOT_FOUND_MSG + reservationId);
        then(reservationRepository).should().findById(reservationId);
    }

    @DisplayName("예약변경 정보를 입력하면, 예약이 변경된다.")
    @Test
    void givenReservationUpdateInfo_whenUpdatingReservation_thenUpdatedReservation() {
        // Given
        ReservationDto dto = createReservationDto("증상업데이트");
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        Reservation reservation = createReservation();

        given(reservationRepository.getReferenceById(dto.id())).willReturn(reservation);
        given(patientRepository.getReferenceById(dto.patientDto().id())).willReturn(patient);
        given(doctorRepository.getReferenceById(dto.doctorDto().id())).willReturn(doctor);

        // When
        sut.updateReservation(dto);

        // Then
        assertThat(reservation)
                .hasFieldOrPropertyWithValue("symptom", dto.symptom())
                .hasFieldOrPropertyWithValue("reservationDate", dto.reservationDate())
                .hasFieldOrPropertyWithValue("reservationTime", dto.reservationTime());
//                .hasFieldOrPropertyWithValue("doctor", doctor)
//                .hasFieldOrPropertyWithValue("patient", patient);
        then(patientRepository).should().getReferenceById(dto.patientDto().id());
        then(doctorRepository).should().getReferenceById(dto.doctorDto().id());
    }

    @DisplayName("없는 예약변경 정보를 입력하면, 로그를 남긴다.")
    @Test
    void givenReservationUpdateInfo_whenUpdatingReservation_thenThrowsException() {
        // Given
        given(reservationRepository.getReferenceById(anyLong())).willThrow(new EntityNotFoundException());

        // When
        sut.updateReservation(createReservationDto());

        // Then
        then(reservationRepository).should().getReferenceById(anyLong());
        then(patientRepository).shouldHaveNoInteractions();
        then(doctorRepository).shouldHaveNoInteractions();
    }

    @DisplayName("삭제할 예약 id와 환자 id를 주면, 해당 예약을 취소한다.")
    @Test
    void givenReservationIdAndPatientId_whenCancelReservation_thenDeletedReservation() {
        // Given
        Long reservationId = 1L;
        Long patientId = 2L;

        // When
        sut.cancelReservation(reservationId, patientId);

        // Then
        then(reservationRepository).should().deleteByIdAndPatient_Id(reservationId, patientId);
    }


    private Reservation createReservation() {
        Reservation reservation = Reservation.of("증상1",
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                createDoctor(),
                createPatient());

        ReflectionTestUtils.setField(reservation, "id", 1L);

        return reservation;
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

    private Patient createPatient() {
        Patient patient = Patient.builder()
                .email("환자이메일@email.com")
                .encryptedPassword("password2")
                .phoneNumber("010-1234-5678")
                .build();

        ReflectionTestUtils.setField(patient, "id", 1L);

        return patient;
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

    private ReservationDto createReservationDto(String symptom) {
        return ReservationDto.of(
                1L,
                symptom,
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                PatientDto.from(createPatient()),
                DoctorDto.from(createDoctor())
        );
    }
}