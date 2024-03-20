package com.example.healthcare_v2.domain.reservation.repository;

import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    void deleteByIdAndPatient_Id(Long reservationId, Long patientId);

    Page<Reservation> findAllByDeletedAtIsNull(Pageable pageable);
}
