package com.example.healthcare_v2.domain.diagnosis.service;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.dto.DoctorDto;
import com.example.healthcare_v2.domain.diagnosis.dto.PatientDto;
import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.diagnosis.exception.DiagnosisNotFoundException;
import com.example.healthcare_v2.domain.diagnosis.repository.DiagnosisRepository;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.doctor.service.DoctorService;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import com.example.healthcare_v2.domain.patient.service.PatientService;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@DisplayName("Diagnosis - 테스트")
@ExtendWith(MockitoExtension.class)
class DiagnosisServiceTest {
    @InjectMocks private DiagnosisService sut;
    @Mock private DiagnosisRepository diagnosisRepository;
    @Mock private PatientService patientService;
    @Mock private DoctorService doctorService;

    @DisplayName("진단 id를 입력하면, 진단 정보를 불러온다.")
    @Test
    void givenDiagnosisId_whenFindDiagnosis_thenReturnsDiagnosis() {
        // Given
        Long diagnosisId = 1L;
        Diagnosis diagnosis = createDiagnosis();
        given(diagnosisRepository.findById(diagnosisId)).willReturn(Optional.ofNullable(diagnosis));

        // When
        Diagnosis resultDiagnosis = sut.findById(diagnosisId);

        // Then
        assertThat(resultDiagnosis).isEqualTo(diagnosis);
        then(diagnosisRepository).should().findById(diagnosisId);
    }

    @DisplayName("없는 진단 id를 입력하면, 예외를 발생시킨다.")
    @Test
    void givenDiagnosisId_whenFindDiagnosis_thenThrowsException() {
        // Given
        Long diagnosisId = 1L;
        Diagnosis diagnosis = createDiagnosis();
        given(diagnosisRepository.findById(diagnosisId)).willReturn(Optional.empty());

        // When
        DiagnosisNotFoundException exception = assertThrows(DiagnosisNotFoundException.class,
                () -> sut.findById(diagnosisId)
        );

        // Then
        assertThat(exception).isInstanceOf(DiagnosisNotFoundException.class);
        then(diagnosisRepository).should().findById(diagnosisId);
    }

    @DisplayName("삭제되지 않은 진단 id를 입력하면, 진단 정보를 불러온다.")
    @Test
    void givenDiagnosisId_whenFindActiveDiagnosis_thenReturnsDiagnosis() {
        // Given
        Long diagnosisId = 1L;
        Diagnosis diagnosis = createDiagnosis();
        given(diagnosisRepository.findById(diagnosisId)).willReturn(Optional.ofNullable(diagnosis));

        // When
        Diagnosis resultDiagnosis = sut.findActiveDiagnosisById(diagnosisId);

        // Then
        assertThat(resultDiagnosis).isEqualTo(diagnosis);
        then(diagnosisRepository).should().findById(diagnosisId);
    }

    @DisplayName("삭제된 진단 id를 입력하면, 예외를 발생시킨다.")
    @Test
    void givenDiagnosisId_whenFindActiveDiagnosis_thenThrowsException() {
        // Given
        Long diagnosisId = 1L;
        Diagnosis diagnosis = createDeletedDiagnosis();
        given(diagnosisRepository.findById(diagnosisId)).willReturn(Optional.ofNullable(diagnosis));

        // When
        DiagnosisNotFoundException exception = assertThrows(DiagnosisNotFoundException.class,
                () -> sut.findActiveDiagnosisById(diagnosisId)
        );

        // Then
        assertThat(exception).isInstanceOf(DiagnosisNotFoundException.class);
        then(diagnosisRepository).should().findById(diagnosisId);
    }


    @DisplayName("진단정보를 입력하면, 진단정보가 저장된다.")
    @Test
    void givenDiagnosisInfo_whenSavingDiagnosis_thenSavedDiagnosis() {
        // Given
        DiagnosisDto dto = createDiagnosisDto();
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        Diagnosis diagnosis = createDiagnosis();
        given(patientService.findById(dto.patientDto().id())).willReturn(patient);
        given(doctorService.findById(dto.doctorDto().id())).willReturn(doctor);
        given(diagnosisRepository.save(any(Diagnosis.class))).willReturn(diagnosis);

        // When
        DiagnosisDto diagnosisDto = sut.saveDiagnosis(dto);

        // Then
        then(patientService).should().findById(dto.patientDto().id());
        then(doctorService).should().findById(dto.doctorDto().id());
        then(diagnosisRepository).should().save(any(Diagnosis.class));
    }

    @DisplayName("모든 진단를 조회하면, 페이징된 진단정보들을 반환한다.")
    @Test
    void givenPageInfo_whenSearchingDiagnoses_thenDiagnoses() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(diagnosisRepository.findAllByDeletedAtIsNull(pageable)).willReturn(Page.empty());

        // When
        Page<DiagnosisDto> diagnosis = sut.getDiagnoses(pageable);

        // Then
        assertThat(diagnosis).isEqualTo(Page.empty());
        then(diagnosisRepository).should().findAllByDeletedAtIsNull(pageable);
    }

    @DisplayName("환자 id로 진단를 조회하면, 페이징 된 환자 id에 해당되는 진단정보들을 반환한다.")
    @Test
    void givenPageInfoAndPatientId_whenSearchingDiagnosesByPatient_thenDiagnoses() {
        // Given
        Long patientId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        given(diagnosisRepository.findByPatient_id(pageable, patientId)).willReturn(Page.empty());

        // When
        Page<DiagnosisDto> diagnosis = sut.getDiagnosesByPatient(pageable, patientId);

        // Then
        assertThat(diagnosis).isEqualTo(Page.empty());
        then(diagnosisRepository).should().findByPatient_id(pageable, patientId);
    }

    @DisplayName("진단변경 정보를 입력하면, 진단 정보가 변경된다.")
    @Test
    void givenDiagnosisUpdateInfo_whenUpdatingDiagnosis_thenUpdatedDiagnosis() {
        // Given
        DiagnosisDto dto = createDiagnosisDto("질병업데이트", "질병내용업데이트");
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        Diagnosis diagnosis = createDiagnosis();

        given(diagnosisRepository.findById(dto.id())).willReturn(Optional.ofNullable(diagnosis));
        given(patientService.findById(dto.patientDto().id())).willReturn(patient);
        given(doctorService.findById(dto.doctorDto().id())).willReturn(doctor);

        // When
        DiagnosisDto diagnosisDto = sut.updateDiagnosis(dto);

        // Then
        assertThat(diagnosis)
                .hasFieldOrPropertyWithValue("disease", dto.disease())
                .hasFieldOrPropertyWithValue("content", dto.content())
                .hasFieldOrPropertyWithValue("doctor", doctor)
                .hasFieldOrPropertyWithValue("patient", patient);
        then(diagnosisRepository).should().findById(dto.id());
        then(patientService).should().findById(dto.patientDto().id());
        then(doctorService).should().findById(dto.doctorDto().id());
    }

    @DisplayName("삭제할 진단 id를 주면, 해당 진단를 삭제한다.")
    @Test
    void givenDiagnosisId_whenDeleteDiagnosis_thenDeletedDiagnosis() {
        // Given
        Long diagnosisId = 1L;
        Diagnosis diagnosis = createDiagnosis();
        given(diagnosisRepository.findById(diagnosisId)).willReturn(Optional.ofNullable(diagnosis));

        // When
        sut.deleteDiagnosis(diagnosisId);

        // Then
        assertThat(diagnosis.getDeletedAt()).isNotNull();
        then(diagnosisRepository).should().findById(diagnosisId);
    }

    private Diagnosis createDiagnosis() {
        Diagnosis diagnosis = Diagnosis.of(
                "질병1",
                "질병내용1",
                createDoctor(),
                createPatient()
        );

        ReflectionTestUtils.setField(diagnosis, "id", 1L);

        return diagnosis;
    }

    private Diagnosis createDeletedDiagnosis() {
        Diagnosis diagnosis = Diagnosis.of(
                "질병1",
                "질병내용1",
                createDoctor(),
                createPatient()
        );

        ReflectionTestUtils.setField(diagnosis, "id", 1L);
        ReflectionTestUtils.setField(diagnosis, "deletedAt",
                LocalDateTime.of(2023, 3, 24, 18, 00, 20));

        return diagnosis;
    }

    private DiagnosisDto createDiagnosisDto(String disease, String content) {
        return DiagnosisDto.of(
                disease,
                content,
                PatientDto.from(createPatient()),
                DoctorDto.from(createDoctor())
        );
    }

    private DiagnosisDto createDiagnosisDto() {
        return DiagnosisDto.of(
                "질병1",
                "질병내용1",
                PatientDto.from(createPatient()),
                DoctorDto.from(createDoctor())
        );
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
}