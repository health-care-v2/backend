package com.example.healthcare_v2.domain.diagnosis.dto;

import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;

import java.util.List;

public record DiagnosisDto(
        Long id,
        String disease,
        String content,
        List<DiagnosisImageDto> diagnosisImageDtos,
        List<PrescriptionDto> prescriptionDtos,
        PatientDto patientDto,
        DoctorDto doctorDto
) {

    public static DiagnosisDto of(String disease,
                                  String content,
                                  List<DiagnosisImageDto> diagnosisImageDtos,
                                  List<PrescriptionDto> prescriptionDtos,
                                  PatientDto patientDto,
                                  DoctorDto doctorDto
    ) {
        return new DiagnosisDto(null, disease, content, diagnosisImageDtos, prescriptionDtos, patientDto, doctorDto);
    }

    public static DiagnosisDto of(String disease,
                                  String content,
                                  PatientDto patientDto,
                                  DoctorDto doctorDto
    ) {
        return new DiagnosisDto(null, disease, content, null, null, patientDto, doctorDto);
    }

    public Diagnosis toEntity(Patient patient, Doctor doctor) {
        return Diagnosis.of(disease, content, doctor, patient);
    }
}
