package com.example.healthcare_v2.domain.diary.service;

import com.example.healthcare_v2.domain.diary.controller.request.CreateDiaryRequest;
import com.example.healthcare_v2.domain.diary.repository.DiaryRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final PatientService patientService;

    public void create(CreateDiaryRequest request, Long userId) {
        Patient patient = patientService.findById(userId);
        diaryRepository.save(request.toEntity(patient));
    }
}
