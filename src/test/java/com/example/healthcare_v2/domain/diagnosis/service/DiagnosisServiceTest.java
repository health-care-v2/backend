package com.example.healthcare_v2.domain.diagnosis.service;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.dto.DoctorDto;
import com.example.healthcare_v2.domain.diagnosis.dto.PatientDto;
import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.diagnosis.repository.DiagnosisRepository;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@DisplayName("Diagnosis - 테스트")
@ExtendWith(MockitoExtension.class)
class DiagnosisServiceTest {
    @InjectMocks private DiagnosisService sut;
    @Mock private DiagnosisRepository diagnosisRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;

    @DisplayName("진료정보를 입력하면, 진료정보가 저장된다.")
    @Test
    void givenDiagnosisInfo_whenSavingDiagnosis_thenSavedDiagnosis() {
        // Given
        DiagnosisDto dto = createDiagnosisDto();
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        given(patientRepository.getReferenceById(dto.patientDto().id())).willReturn(patient);
        given(doctorRepository.getReferenceById(dto.doctorDto().id())).willReturn(doctor);

        // When
        sut.saveDiagnosis(dto);

        // Then
        then(patientRepository).should().getReferenceById(dto.patientDto().id());
        then(doctorRepository).should().getReferenceById(dto.doctorDto().id());
        then(diagnosisRepository).should().save(any(Diagnosis.class));
    }

    @DisplayName("모든 진료를 조회하면, 페이징된 진료정보들을 반환한다.")
    @Test
    void givenPageInfo_whenSearchingDiagnoses_thenDiagnoses() {
        // Given
        Pageable pageable = Pageable.ofSize(10);
        given(diagnosisRepository.findAll(pageable)).willReturn(Page.empty());

        // When
        Page<DiagnosisDto> diagnosis = sut.getDiagnoses(pageable);

        // Then
        assertThat(diagnosis).isEqualTo(Page.empty());
        then(diagnosisRepository).should().findAll(pageable);
    }

    @DisplayName("환자 id로 진료를 조회하면, 페이징 된 환자 id에 해당되는 진료정보들을 반환한다.")
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

    @DisplayName("진료변경 정보를 입력하면, 진료 정보가 변경된다.")
    @Test
    void givenDiagnosisUpdateInfo_whenUpdatingDiagnosis_thenUpdatedDiagnosis() {
        // Given
        DiagnosisDto dto = createDiagnosisDto("질병업데이트", "질병내용업데이트");
        Patient patient = createPatient();
        Doctor doctor = createDoctor();
        Diagnosis diagnosis = createDiagnosis();

        given(diagnosisRepository.getReferenceById(dto.id())).willReturn(diagnosis);
        given(patientRepository.getReferenceById(dto.patientDto().id())).willReturn(patient);
        given(doctorRepository.getReferenceById(dto.doctorDto().id())).willReturn(doctor);

        // When
        sut.updateDiagnosis(dto);

        // Then
        assertThat(diagnosis)
                .hasFieldOrPropertyWithValue("disease", dto.disease())
                .hasFieldOrPropertyWithValue("content", dto.content());
        then(diagnosisRepository).should().getReferenceById(dto.id());
        then(patientRepository).should().getReferenceById(dto.patientDto().id());
        then(doctorRepository).should().getReferenceById(dto.doctorDto().id());
    }

    @DisplayName("진료변경 정보를 입력하면, 진료 정보가 변경된다.")
    @Test
    void givenDiagnosisUpdateInfo_whenUpdatingDiagnosis_thenLogsWarning() {
        // Given
        DiagnosisDto dto = createDiagnosisDto("질병업데이트", "질병내용업데이트");

        given(diagnosisRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        sut.updateDiagnosis(dto);

        // Then
        then(diagnosisRepository).should().getReferenceById(dto.id());
        then(patientRepository).shouldHaveNoInteractions();
        then(doctorRepository).shouldHaveNoInteractions();
    }

    @DisplayName("삭제할 진료 id를 주면, 해당 진료를 삭제한다.")
    @Test
    void givenDiagnosisId_whenDeleteDiagnosis_thenDeletedDiagnosis() {
        // Given
        Long diagnosis = 1L;

        // When
        sut.deleteDiagnosis(diagnosis);

        // Then
        then(diagnosisRepository).should().deleteById(diagnosis);
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