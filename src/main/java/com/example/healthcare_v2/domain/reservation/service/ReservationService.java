package com.example.healthcare_v2.domain.reservation.service;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.doctor.service.DoctorService;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.patient.service.PatientService;
import com.example.healthcare_v2.domain.reservation.dto.ReservationDto;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import com.example.healthcare_v2.domain.reservation.exception.ReservationNotFoundException;
import com.example.healthcare_v2.domain.reservation.exception.ReservationPatientNotMatchException;
import com.example.healthcare_v2.domain.reservation.repository.ReservationRepository;
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
    private final PatientService patientService;
    private final DoctorService doctorService;

    @Transactional(readOnly = true)
    public Reservation findById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(ReservationNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Reservation findActiveReservationById(Long reservationId) {
        Reservation reservation = findById(reservationId);

        if (reservation.isDeleted()) {
            throw new ReservationNotFoundException();
        }

        return reservation;
    }

    public ReservationDto saveReservation(ReservationDto dto) {
        Patient patient = patientService.findById(dto.patientDto().id());
        Doctor doctor = doctorService.findById(dto.doctorDto().id());

        return ReservationDto.from(reservationRepository.save(dto.toEntity(patient, doctor)));
    }

    @Transactional(readOnly = true)
    public Page<ReservationDto> getReservations(Pageable pageable) {
        return reservationRepository.findAllByDeletedAtIsNull(pageable).map(ReservationDto::from);
    }

    @Transactional(readOnly = true)
    public ReservationDto getReservation(Long reservationId) {
        return ReservationDto.from(findActiveReservationById(reservationId));
    }

    public ReservationDto updateReservation(ReservationDto dto) {
        Reservation reservation = findActiveReservationById(dto.id());
        Patient patient = patientService.findById(dto.patientDto().id());
        Doctor doctor = doctorService.findById(dto.doctorDto().id());

        if (!reservation.getPatient().equals(patient)) {
            throw new ReservationPatientNotMatchException();
        }

        reservation.changeReservation(dto.symptom(), dto.reservationDate(),
                dto.reservationTime(), doctor);

        return ReservationDto.from(reservation);
    }

    public void cancelReservation(Long reservationId, Long patientId) {
        Reservation reservation = findActiveReservationById(reservationId);
        Patient patient = patientService.findById(patientId);

        if (!reservation.getPatient().equals(patient)) {
            throw new ReservationPatientNotMatchException();
        }
        reservation.delete();
    }

}

