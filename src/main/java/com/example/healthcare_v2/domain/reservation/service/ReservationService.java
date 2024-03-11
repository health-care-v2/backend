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
        Patient patient = patientRepository.getReferenceById(dto.patientDto().id());
        Doctor doctor = doctorRepository.getReferenceById(dto.doctorDto().id());
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
        try {
            Reservation reservation = reservationRepository.getReferenceById(dto.id());
            Patient patient = patientRepository.getReferenceById(dto.patientDto().id());

            log.info("reservation = {} ", reservation.getPatient().getId());
            log.info("patient = {}, ", patient.getId());

            if (reservation.getPatient().equals(patient)) {
                reservation.changeReservation(dto.symptom(), dto.reservationDate(),
                        dto.reservationTime(), doctorRepository.getReferenceById(dto.doctorDto().id()));
            }
        } catch (EntityNotFoundException e) {
            log.warn("예약 업데이트 실패. {}", e.getLocalizedMessage());
        }
    }

    public void cancelReservation(Long reservationId, Long patientId) {
        reservationRepository.deleteByIdAndPatient_Id(reservationId, patientId);
    }
}
