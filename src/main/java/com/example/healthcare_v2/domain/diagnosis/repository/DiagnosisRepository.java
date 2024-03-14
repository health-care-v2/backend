package com.example.healthcare_v2.domain.diagnosis.repository;

import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {
}
