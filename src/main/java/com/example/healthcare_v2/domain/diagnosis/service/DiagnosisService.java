package com.example.healthcare_v2.domain.diagnosis.service;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.diagnosis.exception.DiagnosisNotFoundException;
import com.example.healthcare_v2.domain.diagnosis.repository.DiagnosisRepository;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.service.DoctorService;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
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
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional(readOnly = true)
    public Diagnosis findById(Long diagnosisId) {
        return diagnosisRepository.findById(diagnosisId)
                .orElseThrow(DiagnosisNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Diagnosis findActiveDiagnosisById(Long diagnosisId) {
        Diagnosis diagnosis = findById(diagnosisId);

        if (diagnosis.isDeleted()) {
            throw new DiagnosisNotFoundException();
        }

        return diagnosis;
    }

    public DiagnosisDto saveDiagnosis(DiagnosisDto dto) {
        Doctor doctor = doctorService.findById(dto.doctorDto().id());
        Patient patient = patientService.findById(dto.patientDto().id());

        return DiagnosisDto.from(diagnosisRepository.save(dto.toEntity(patient, doctor)));
    }

    @Transactional(readOnly = true)
    public Page<DiagnosisDto> getDiagnoses(Pageable pageable) {
        return diagnosisRepository.findAllByDeletedAtIsNull(pageable).map(DiagnosisDto::from);
    }

    @Transactional(readOnly = true)
    public Page<DiagnosisDto> getDiagnosesByPatient(Pageable pageable, Long patientId) {
        return diagnosisRepository.findByPatient_id(pageable, patientId).map(DiagnosisDto::from);
    }

    public DiagnosisDto updateDiagnosis(DiagnosisDto dto) {
        Diagnosis diagnosis = findActiveDiagnosisById(dto.id());
        Patient patient = patientService.findById(dto.patientDto().id());
        Doctor doctor = doctorService.findById(dto.doctorDto().id());

        diagnosis.changeDiagnosis(dto.disease(), dto.content(), patient, doctor);

        return DiagnosisDto.from(diagnosis);
    }

    public void deleteDiagnosis(Long diagnosisId) {
        Diagnosis diagnosis = findActiveDiagnosisById(diagnosisId);
        diagnosis.delete();
    }

}
