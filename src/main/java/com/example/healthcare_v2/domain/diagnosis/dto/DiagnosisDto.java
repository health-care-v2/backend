package com.example.healthcare_v2.domain.diagnosis.dto;

import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;

import java.util.List;
import java.util.stream.Collectors;

public record DiagnosisDto(
        Long id,
        String disease,
        String content,
        List<DiagnosisImageDto> diagnosisImageDtos,
        List<PrescriptionDto> prescriptionDtos,
        PatientDto patientDto,
        DoctorDto doctorDto
) {

    public static DiagnosisDto of(Long id,
                                  String disease,
                                  String content,
                                  List<DiagnosisImageDto> diagnosisImageDtos,
                                  List<PrescriptionDto> prescriptionDtos,
                                  PatientDto patientDto,
                                  DoctorDto doctorDto
    ) {
        return new DiagnosisDto(id, disease, content, diagnosisImageDtos, prescriptionDtos, patientDto, doctorDto);
    }

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

    public static DiagnosisDto from(Diagnosis diagnosis) {
        return new DiagnosisDto(
                diagnosis.getId(),
                diagnosis.getDisease(),
                diagnosis.getContent(),
                diagnosis.getImages().stream()
                        .map(DiagnosisImageDto::from)
                        .collect(Collectors.toList()),
                diagnosis.getPrescriptions().stream()
                        .map(PrescriptionDto::from)
                        .collect(Collectors.toList()),
                PatientDto.from(diagnosis.getPatient()),
                DoctorDto.from(diagnosis.getDoctor())
        );
    }
}
