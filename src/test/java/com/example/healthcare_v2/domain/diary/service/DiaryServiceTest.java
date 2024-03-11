package com.example.healthcare_v2.domain.diary.service;

import com.example.healthcare_v2.domain.diary.controller.request.CreateDiaryRequest;
import com.example.healthcare_v2.domain.diary.controller.request.UpdateDiaryRequest;
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


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
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

    @DisplayName("일기 수정")
    @Test
    void updateDiary(){
        // given
        Long userId = -1L;
        Long diaryId = -1L;

        Patient patient = Patient.builder()
                .email("test@naver.com")
                .name("test1")
                .encryptedPassword("password")
                .build();

        Diary diary = Diary.builder()
                .title("제목")
                .status(4.33f)
                .content("내용")
                .takeMedicine("아침,점심")
                .isWalk(true)
                .isStretching(true)
                .isPublic(false)
                .patient(patient)
                .likeCount(0L)
                .build();

        UpdateDiaryRequest request = new UpdateDiaryRequest(
                "제목 update",
                true,
                false,
                "아침,점심,저녁",
                4.33f,
                "복통",
                true
        );

        given(patientService.findActivePatientById(userId)).willReturn(patient);
        given(diaryRepository.findById(diaryId)).willReturn(Optional.ofNullable(diary));

        // when
        diaryService.update(request,userId,diaryId);

        // then
        assertThat(diary.getTitle()).isEqualTo("제목 update");
    }

    @DisplayName("일기 삭제")
    @Test
    void deleteDiary(){
        // given
        Long userId = -1L;
        Long diaryId = -1L;

        Patient patient = Patient.builder()
                .email("test@naver.com")
                .name("test1")
                .encryptedPassword("password")
                .build();

        Diary diary = Diary.builder()
                .title("제목")
                .status(4.33f)
                .content("내용")
                .takeMedicine("아침,점심")
                .isWalk(true)
                .isStretching(true)
                .isPublic(false)
                .patient(patient)
                .likeCount(0L)
                .build();

        given(patientService.findActivePatientById(userId)).willReturn(patient);
        given(diaryRepository.findById(diaryId)).willReturn(Optional.ofNullable(diary));

        // when, then
        assertThatNoException()
                .isThrownBy(()->diaryService.delete(diaryId,userId));
    }
}
