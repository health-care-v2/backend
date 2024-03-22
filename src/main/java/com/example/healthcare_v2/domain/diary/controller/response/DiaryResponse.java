package com.example.healthcare_v2.domain.diary.controller.response;

import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.patient.dto.PatientDto;

import java.util.List;
import java.util.stream.Collectors;


public record DiaryResponse(
        String title,

        Boolean isWalk,

        Boolean isStretching,

        String takeMedicine,

        Float status,   // 0~5 별점 계산

        String content,

        Boolean isPublic,

        Long likeCount,
        PatientDto patientDto
) {
    public static List<DiaryResponse> fromEntities(List<Diary> diaries) {
        return diaries.stream()
                .map(diary ->
                        new DiaryResponse(
                                diary.getTitle(),
                                diary.getIsWalk(),
                                diary.getIsStretching(),
                                diary.getTakeMedicine(),
                                diary.getStatus(),
                                diary.getContent(),
                                diary.getIsPublic(),
                                diary.getLikeCount(),
                                PatientDto.fromEntity(diary.getPatient())
                        )
                )
                .collect(Collectors.toList());
    }
}
