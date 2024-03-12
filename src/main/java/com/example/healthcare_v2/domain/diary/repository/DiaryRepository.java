package com.example.healthcare_v2.domain.diary.repository;

import com.example.healthcare_v2.domain.diary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d FROM Diary d WHERE d.isPublic = true")
    List<Diary> findByIsPublicTrue();
}
