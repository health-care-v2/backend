package com.example.healthcare_v2.domain.diagnosis.service;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.diagnosis.repository.DiagnosisRepository;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class DiagnosisService {
    private final DiagnosisRepository diagnosisRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public void saveDiagnosis(DiagnosisDto dto) {
        Doctor doctor = doctorRepository.getReferenceById(dto.doctorDto().id());
        Patient patient = patientRepository.getReferenceById(dto.patientDto().id());
        diagnosisRepository.save(dto.toEntity(patient, doctor));
    }

    @Transactional(readOnly = true)
    public Page<DiagnosisDto> getDiagnoses(Pageable pageable) {
        return diagnosisRepository.findAll(pageable).map(DiagnosisDto::from);
    }

    @Transactional(readOnly = true)
    public Page<DiagnosisDto> getDiagnosesByPatient(Pageable pageable, Long patientId) {
        return diagnosisRepository.findByPatient_id(pageable, patientId).map(DiagnosisDto::from);
    }

    public void updateDiagnosis(DiagnosisDto dto) {
        try {
            Diagnosis diagnosis = diagnosisRepository.getReferenceById(dto.id());
            Patient patient = patientRepository.getReferenceById(dto.patientDto().id());
            Doctor doctor = doctorRepository.getReferenceById(dto.doctorDto().id());

            diagnosis.changeDiagnosis(dto.disease(), dto.content(), patient, doctor);
        } catch (EntityNotFoundException e) {
            log.warn("진료 업데이트 실패. {}", e.getLocalizedMessage());
        }
    }

}
