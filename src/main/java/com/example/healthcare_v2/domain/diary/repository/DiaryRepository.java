package com.example.healthcare_v2.domain.diary.repository;

import com.example.healthcare_v2.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
