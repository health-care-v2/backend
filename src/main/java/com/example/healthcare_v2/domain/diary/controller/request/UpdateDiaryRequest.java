package com.example.healthcare_v2.domain.diary.controller.request;

import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import jakarta.validation.constraints.NotNull;

public record UpdateDiaryRequest(
        String title,
        @NotNull
        Boolean isWalk,
        @NotNull
        Boolean isStretching,
        String takeMedicine,
        Float status,   // 0~5 별점 계산
        String content,
        @NotNull
        Boolean isPublic
) {

    public Diary toEntity(Patient patient) {
        return Diary.builder()
                .title(title)
                .isWalk(isWalk)
                .isStretching(isStretching)
                .takeMedicine(takeMedicine)
                .status((float) Math.round(status))
                .content(content)
                .isPublic(isPublic)
                .patient(patient)
                .build();
    }
}
