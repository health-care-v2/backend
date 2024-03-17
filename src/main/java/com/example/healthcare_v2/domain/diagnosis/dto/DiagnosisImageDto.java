package com.example.healthcare_v2.domain.diagnosis.dto;

import com.example.healthcare_v2.domain.diagnosis.entity.DiagnosisImage;

public record DiagnosisImageDto(
        Long id,
        String path
) {
    public static DiagnosisImageDto from(DiagnosisImage diagnosisImage) {
        return new DiagnosisImageDto(
                diagnosisImage.getId(),
                diagnosisImage.getPath()
        );
    }

}
