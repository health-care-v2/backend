package com.example.healthcare_v2.domain.diagnosis.dto.request;


import com.example.healthcare_v2.domain.diagnosis.dto.DiagnosisDto;
import com.example.healthcare_v2.domain.diagnosis.dto.DoctorDto;
import com.example.healthcare_v2.domain.diagnosis.dto.PatientDto;
import com.example.healthcare_v2.domain.diagnosis.dto.PrescriptionDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
                PatientDto.of(patientId),
                DoctorDto.of(doctorId)
        );
    }

}
