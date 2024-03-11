package com.example.healthcare_v2.domain.diary.service;

import com.example.healthcare_v2.domain.diary.controller.request.CreateDiaryRequest;
import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.diary.repository.DiaryRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DiaryServiceTest {

    @InjectMocks
    private DiaryService diaryService;
    @Mock
    private DiaryRepository diaryRepository;
    @Mock
    private PatientService patientService;

    @DisplayName("일기 등록")
    @Test
    void postDiary() {
        // given
        Long userId = -1L;
        Patient patient = Patient.builder()
                .email("test@naver.com")
                .name("test1")
                .encryptedPassword("password")
                .build();

        CreateDiaryRequest request = new CreateDiaryRequest(
                "제목",
                true,
                false,
                "아침,점심,저녁",
                4.33f,
                "복통",
                true
        );

        given(patientService.findById(userId)).willReturn(patient);

        // when
        diaryService.create(request,userId);

        // then
        verify(diaryRepository,atLeastOnce()).save(any(Diary.class));
    }
}
