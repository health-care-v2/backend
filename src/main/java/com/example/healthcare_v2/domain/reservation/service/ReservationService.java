package com.example.healthcare_v2.domain.reservation.service;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.repository.DoctorRepository;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.repository.PatientRepository;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import com.example.healthcare_v2.domain.reservation.repository.ReservationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public void saveReservation(ReservationDto dto) {
        Patient patient = patientRepository.getReferenceById(dto.patientId());
        Doctor doctor = doctorRepository.getReferenceById(dto.doctorId());
        reservationRepository.save(dto.toEntity(patient, doctor));
    }

    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable).map(ReservationDto::from);
    }

    @Transactional(readOnly = true)
    public ReservationDto getReservation(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .map(ReservationDto::from)
                .orElseThrow(() -> new EntityNotFoundException("예약이 없습니다. - reservationId: " + reservationId));
    }

    public void updateReservation(ReservationDto dto) {
        Reservation reservation = reservationRepository.findById(dto.id()).get();
        Patient patient = patientRepository.findById(dto.patientId()).get();

        try {
            if (reservation.getPatient().equals(patient)) {
                if (dto.doctorId() != null) {
                    reservation.setDoctor(doctorRepository.getReferenceById(dto.doctorId()));
                }

                if (dto.symptom() != null) {
                    reservation.setSymptom(dto.symptom());
                }

                if (dto.reservationDate() != null) {
                    reservation.setReservationDate(dto.reservationDate());
                }

                if (dto.reservationTime() != null) {
                    reservation.setReservationTime(dto.reservationTime());
                }
            }
        } catch (EntityNotFoundException e) {
            log.warn("예약 업데이트 실패. {}", e.getLocalizedMessage());
        }
    }

    public void cancelReservation(Long reservationId) {
        reservationRepository.deleteById(reservationId);
    }
}
