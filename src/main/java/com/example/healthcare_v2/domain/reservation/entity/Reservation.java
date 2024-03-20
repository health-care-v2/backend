package com.example.healthcare_v2.domain.reservation.entity;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symptom;

    private LocalDate reservationDate;

    private LocalTime reservationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public void changeReservation(String symptom, LocalDate reservationDate, LocalTime reservationTime, Doctor doctor) {
        this.symptom = symptom;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.doctor = doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }


    private Reservation(String symptom,
                        LocalDate reservationDate,
                        LocalTime reservationTime,
                        Doctor doctor,
                        Patient patient) {
        this.symptom = symptom;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.doctor = doctor;
        this.patient = patient;
    }

    public static Reservation of(String symptom,
                                 LocalDate reservationDate,
                                 LocalTime reservationTime,
                                 Doctor doctor,
                                 Patient patient) {
        return new Reservation(symptom, reservationDate, reservationTime, doctor, patient);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
