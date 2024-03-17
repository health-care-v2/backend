package com.example.healthcare_v2.domain.diagnosis.dto.response;

import com.example.healthcare_v2.domain.diagnosis.dto.*;

import java.util.List;

public record DiagnosisResponseDto(
        Long id,
        String disease,
        String content,
        List<DiagnosisImageDto> diagnosisImageDtos,
        List<PrescriptionDto> prescriptionDtos,
        PatientDto patientDto,
        DoctorDto doctorDto
) {

    public static DiagnosisResponseDto from(DiagnosisDto diagnosisDto) {
        return new DiagnosisResponseDto(
                diagnosisDto.id(),
                diagnosisDto.disease(),
                diagnosisDto.content(),
                diagnosisDto.diagnosisImageDtos(),
                diagnosisDto.prescriptionDtos(),
                diagnosisDto.patientDto(),
                diagnosisDto.doctorDto()
        );
    }

}
