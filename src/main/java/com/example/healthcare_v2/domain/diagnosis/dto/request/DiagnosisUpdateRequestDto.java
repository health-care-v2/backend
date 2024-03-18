package com.example.healthcare_v2.domain.diagnosis.dto.request;

import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.dto.DoctorDto;
import com.example.healthcare_v2.domain.diagnosis.dto.PatientDto;
import jakarta.validation.constraints.NotNull;

public record DiagnosisUpdateRequestDto(
        @NotNull String disease,
        @NotNull String content,
        @NotNull Long patientId,
        @NotNull Long doctorId
) {

    public DiagnosisDto toDto(Long diagnosisId) {
        return DiagnosisDto.of(diagnosisId, disease, content, PatientDto.from(patientId), DoctorDto.from(doctorId));
    }

}
