package com.example.healthcare_v2.domain.patient.repository;

import com.example.healthcare_v2.domain.patient.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
