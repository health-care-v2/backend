package com.example.healthcare_v2.domain.like.service;

import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.diary.repository.DiaryRepository;
import com.example.healthcare_v2.domain.diary.service.DiaryService;
import com.example.healthcare_v2.domain.like.entity.DiaryLike;
import com.example.healthcare_v2.domain.like.exception.DuplicateWishException;
import com.example.healthcare_v2.domain.like.repository.DiaryLikeRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryLikeService {

    private final DiaryLikeRepository diaryLikeRepository;
    private final PatientService patientService;
    private final DiaryService diaryService;

    public void createWish(Long diaryId, Long userId) {
        Patient patient = patientService.findActivePatientById(userId);
        Diary diary = diaryService.findActiveDiaryById(diaryId);
        if (diaryLikeRepository.existsByPatientAndDiary(patient, diary)) {
            throw new DuplicateWishException();
        }
        diaryLikeRepository.save(DiaryLike.builder()
                .diary(diary)
                .patient(patient)
                .build());
    }
}
