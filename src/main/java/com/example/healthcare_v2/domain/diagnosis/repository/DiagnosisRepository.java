package com.example.healthcare_v2.domain.diagnosis.repository;

import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosisRepository extends JpaRepository<Diagnosis, Long> {

    Page<Diagnosis> findByPatient_id(Pageable pageable, Long patientId);

    Page<Diagnosis> findAllByDeletedAtIsNull(Pageable pageable);
}
