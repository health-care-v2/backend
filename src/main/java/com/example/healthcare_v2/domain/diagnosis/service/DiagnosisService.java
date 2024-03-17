package com.example.healthcare_v2.domain.diagnosis.service;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.repository.DiagnosisRepository;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Page<DiagnosisDto> getDiagnoses(Pageable pageable) {
        return diagnosisRepository.findAll(pageable).map(DiagnosisDto::from);
    }

    public Page<DiagnosisDto> getDiagnosesByPatient(Pageable pageable, Long patientId) {
        return diagnosisRepository.findByPatient_id(pageable, patientId).map(DiagnosisDto::from);
    }

}
