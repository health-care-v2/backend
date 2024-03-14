package com.example.healthcare_v2.domain.diagnosis.service;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.repository.DiagnosisRepository;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
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
}
