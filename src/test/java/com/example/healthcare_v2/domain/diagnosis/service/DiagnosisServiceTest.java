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
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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