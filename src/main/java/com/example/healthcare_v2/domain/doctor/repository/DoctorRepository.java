package com.example.healthcare_v2.domain.doctor.repository;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByEmail(String email);
}
