package com.example.healthcare_v2.domain.diary.service;

import com.example.healthcare_v2.domain.diary.controller.request.CreateDiaryRequest;
import com.example.healthcare_v2.domain.diary.controller.request.UpdateDiaryRequest;
import com.example.healthcare_v2.domain.diary.controller.response.DiaryResponse;
import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.diary.exception.DiaryNotFoundException;
import com.example.healthcare_v2.domain.diary.exception.UserNotMatchException;
import com.example.healthcare_v2.domain.diary.repository.DiaryRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final PatientService patientService;

    @Transactional(readOnly = true)
    public Diary findById(Long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(DiaryNotFoundException::new);
    }

    public void create(CreateDiaryRequest request, Long userId) {
        Patient patient = patientService.findById(userId);
        diaryRepository.save(request.toEntity(patient));
    }

    public void update(UpdateDiaryRequest request, Long userId, Long diaryId) {
        Patient patient = patientService.findActivePatientById(userId);
        Diary diary = findById(diaryId);
        if (!diary.getPatient().equals(patient)) {
            throw new UserNotMatchException();
        }
        diary.update(request.toEntity(patient));
    }

    public void delete(Long diaryId, Long userId) {
        Patient patient = patientService.findActivePatientById(userId);
        Diary diary = findById(diaryId);
        if (!diary.getPatient().equals(patient)) {
            throw new UserNotMatchException();
        }
        diary.delete();
        diaryRepository.save(diary);
    }

    @Transactional(readOnly = true)
    public List<DiaryResponse> readOnlyPublic(Long userId) {
        patientService.findActivePatientById(userId);
        List<Diary> diaries = diaryRepository.findByIsPublicTrue();
        return DiaryResponse.fromEntities(diaries);
    }
}
