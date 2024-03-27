package com.example.healthcare_v2.domain.reservation.service;


import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.service.DoctorService;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import com.example.healthcare_v2.domain.reservation.dto.DoctorDto;
import com.example.healthcare_v2.domain.reservation.dto.PatientDto;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import com.example.healthcare_v2.domain.reservation.exception.ReservationNotFoundException;
import com.example.healthcare_v2.domain.reservation.exception.ReservationPatientNotMatchException;
import com.example.healthcare_v2.domain.reservation.repository.ReservationRepository;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("ReservationService - 테스트")
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks private ReservationService sut;
    @Mock private ReservationRepository reservationRepository;
    @Mock private PatientService patientService;
    @Mock private DoctorService doctorService;

    @DisplayName("예약 id를 입력하면, 예약 정보를 불러온다.")
    @Test
    void givenReservationId_whenFindReservation_thenReturnsReservation() {
        // Given
        Long reservationId = 1L;
        Reservation reservation = createReservation();
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(reservation));

        // When
        Reservation resultReservation = sut.findById(reservationId);

        // Then
        assertThat(resultReservation).isEqualTo(reservation);
        then(reservationRepository).should().findById(reservationId);
    }

    @DisplayName("없는 예약 id로 조회하면, 예외를 발생시킨다.")
    @Test
    void givenReservationId_whenFindReservation_thenThrowsException() {
        // Given
        Long reservationId = 1L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.empty());

        // When
        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class,
                () -> {
                    sut.findById(reservationId);
                });

        // Then
        assertThat(exception).isInstanceOf(ReservationNotFoundException.class);
        then(reservationRepository).should().findById(reservationId);
    }

    @DisplayName("삭제 되지 않은 예약 id를 입력하면, 예약정보를 불러온다")
    @Test
    void givenDeletedReservationId_whenFindActiveReservation_thenThrowsException() {
        // Given
        Long reservationId = 1L;
        Reservation deltedReservation = createReservation();
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(deltedReservation));

        // When
        Reservation reservation = sut.findActiveReservationById(reservationId);

        // Then
        assertThat(reservation.getDeletedAt()).isNull();
        then(reservationRepository).should().findById(reservationId);
    }

    @DisplayName("삭제된 예약 id를 입력하면, 예외를 발생시킨다.")
    @Test
    void givenReservationId_whenFindActiveReservation_thenReturnsReservation() {
        // Given
        Long reservationId = 1L;
        Reservation deletedReservation = createDeltedReservation();
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(deletedReservation));

        // When
        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class,
                () -> sut.findActiveReservationById(reservationId));

        // Then
        assertThat(exception).isInstanceOf(ReservationNotFoundException.class);
        assertThat(deletedReservation.getDeletedAt()).isNotNull();
        then(reservationRepository).should().findById(reservationId);
    }


    @DisplayName("예약정보를 입력하면, 예약이 완료된다.")
    @Test
    void givenReservationInfo_whenSavingReservation_thenSavedReservation() {
        // Given
        ReservationDto dto = createReservationDto();
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        Reservation reservation = createReservation();
        given(patientService.findById(dto.patientDto().id())).willReturn(patient);
        given(doctorService.findById(dto.doctorDto().id())).willReturn(doctor);
        given(reservationRepository.save(any(Reservation.class))).willReturn(reservation);

        // When
        ReservationDto reservationDto = sut.saveReservation(dto);

        // Then
        then(patientService).should().findById(dto.patientDto().id());
        then(doctorService).should().findById(dto.doctorDto().id());
        then(reservationRepository).should().save(any(Reservation.class));
    }

    @DisplayName("모든 예약을 조회하면, 페이징된 예약정보들을 반환한다.")
    @Test
    void givenPageInfo_whenSearchingReservations_thenReservations() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(reservationRepository.findAllByDeletedAtIsNull(pageable)).willReturn(Page.empty());

        // When
        Page<ReservationDto> reservations = sut.getReservations(pageable);

        // Then
        assertThat(reservations).isEqualTo(Page.empty());
        then(reservationRepository).should().findAllByDeletedAtIsNull(pageable);
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
        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class, () -> {
            sut.getReservation(reservationId);
        });

        assertThat(exception).isInstanceOf(ReservationNotFoundException.class);
        then(reservationRepository).should().findById(reservationId);
    }

    @DisplayName("삭제된 예약 id로 조회하면, 예외가 발생한다.")
    @Test
    void givenDeletedReservationId_whenGetReservation_thenThrowsException() {
        // Given
        Long reservationId = 1L;
        Reservation reservation = createDeltedReservation();
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(reservation));

        // When & Then
        ReservationNotFoundException exception = assertThrows(ReservationNotFoundException.class, () -> {
            sut.getReservation(reservationId);
        });

        assertThat(exception).isInstanceOf(ReservationNotFoundException.class);
        assertThat(reservation).isNotNull();
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

        given(reservationRepository.findById(dto.id())).willReturn(Optional.ofNullable(reservation));
        given(patientService.findById(dto.patientDto().id())).willReturn(patient);
        given(doctorService.findById(dto.doctorDto().id())).willReturn(doctor);

        // When
        sut.updateReservation(dto);

        // Then
        assertThat(reservation)
                .hasFieldOrPropertyWithValue("symptom", dto.symptom())
                .hasFieldOrPropertyWithValue("reservationDate", dto.reservationDate())
                .hasFieldOrPropertyWithValue("reservationTime", dto.reservationTime())
                .hasFieldOrPropertyWithValue("doctor", doctor)
                .hasFieldOrPropertyWithValue("patient", patient);
        then(patientService).should().findById(dto.patientDto().id());
        then(doctorService).should().findById(dto.doctorDto().id());
    }

    @DisplayName("예약 변경시 예약 환자가 다르면, 예외를 발생시킨다.")
    @Test
    void givenReservationUpdateInfo_whenUpdatingReservation_thenThrowsException() {
        // Given
        Patient anotherPatient = createAnotherPatient();
        ReservationDto dto = createReservationDto("증상업데이트", anotherPatient);
        Doctor doctor = createDoctor();
        Reservation reservation = createReservation();
        given(reservationRepository.findById(dto.id())).willReturn(Optional.ofNullable(reservation));
        given(patientService.findById(dto.patientDto().id())).willReturn(anotherPatient);
        given(doctorService.findById(dto.doctorDto().id())).willReturn(doctor);

        // When
        ReservationPatientNotMatchException exception = assertThrows(ReservationPatientNotMatchException.class,
                () -> sut.updateReservation(dto)
        );

        // Then
        assertThat(exception).isInstanceOf(ReservationPatientNotMatchException.class);
        then(reservationRepository).should().findById(dto.id());
        then(patientService).should().findById(dto.patientDto().id());
        then(doctorService).should().findById(dto.doctorDto().id());
    }

    @DisplayName("삭제할 예약 id와 환자 id를 주면, 해당 예약을 취소한다.")
    @Test
    void givenReservationIdAndPatientId_whenCancelReservation_thenDeletedReservation() {
        // Given
        Patient patient = createPatient();
        Reservation reservation = createReservation();
        Long reservationId = 1L;
        Long patientId = 1L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(reservation));
        given(patientService.findById(patientId)).willReturn(patient);

        // When
        sut.cancelReservation(reservationId, patientId);

        // Then
        assertThat(reservation.getDeletedAt()).isNotNull();
        then(reservationRepository).should().findById(reservationId);
        then(patientService).should().findById(patientId);
    }

    @DisplayName("예약 취소시 다른 환자가 취소할 경우, 예외를 발생시킨다.")
    @Test
    void givenReservationIdAndPatientId_whenCancelReservation_thenThrowsException() {
        // Given
        Patient patient = createAnotherPatient();
        Reservation reservation = createReservation();
        Long reservationId = 1L;
        Long patientId = 2L;
        given(reservationRepository.findById(reservationId)).willReturn(Optional.ofNullable(reservation));
        given(patientService.findById(patientId)).willReturn(patient);

        // When
        ReservationPatientNotMatchException exception = assertThrows(ReservationPatientNotMatchException.class,
                () -> sut.cancelReservation(reservationId, patientId)
        );

        // Then
        assertThat(exception).isInstanceOf(ReservationPatientNotMatchException.class);
        then(reservationRepository).should().findById(reservationId);
        then(patientService).should().findById(patientId);
    }

    private Reservation createDeltedReservation() {
        Reservation reservation = Reservation.of("증상1",
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                createDoctor(),
                createPatient());

        ReflectionTestUtils.setField(reservation, "id", 1L);
        ReflectionTestUtils.setField(reservation, "deletedAt",
                LocalDateTime.of(2023, 3, 24, 18, 00, 20));

        return reservation;
    }

    private Reservation createReservation() {
        Reservation reservation = Reservation.of(
                "증상1",
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

    private Patient createAnotherPatient() {
        Patient patient = Patient.builder()
                .email("환자이메일2@email.com")
                .encryptedPassword("password2")
                .phoneNumber("010-1234-5678")
                .build();

        ReflectionTestUtils.setField(patient, "id", 2L);

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

    private ReservationDto createReservationDto(String symptom, Patient anotherPatient) {
        return ReservationDto.of(
                1L,
                symptom,
                LocalDate.of(2024, 02, 18),
                LocalTime.of(18, 00, 20),
                PatientDto.from(anotherPatient),
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