package com.example.healthcare_v2.domain.patient.repository;

import com.example.healthcare_v2.domain.patient.entity.Patient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {

    Optional<Patient> findByEmail(String email);
}
