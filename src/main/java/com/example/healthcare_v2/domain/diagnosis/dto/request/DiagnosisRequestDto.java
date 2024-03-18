package com.example.healthcare_v2.domain.diagnosis.dto.request;


import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.dto.DoctorDto;
import com.example.healthcare_v2.domain.diagnosis.dto.PatientDto;

public record DiagnosisRequestDto(
        String disease,
        String content,
        Long patientId,
        Long doctorId
) {

    public DiagnosisDto toDto() {
        return DiagnosisDto.of(
                disease,
                content,
                PatientDto.from(patientId),
                DoctorDto.from(doctorId)
        );
    }

}
