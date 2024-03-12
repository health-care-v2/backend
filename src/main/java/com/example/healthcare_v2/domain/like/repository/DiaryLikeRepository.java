package com.example.healthcare_v2.domain.like.repository;

import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.like.entity.DiaryLike;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryLikeRepository extends JpaRepository<DiaryLike, Long> {
    Boolean existsByPatientAndDiary(Patient patient, Diary diary);
}
