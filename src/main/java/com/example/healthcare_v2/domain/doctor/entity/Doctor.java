package com.example.healthcare_v2.domain.doctor.entity;

import com.example.healthcare_v2.domain.connect.entity.Connect;
import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import com.example.healthcare_v2.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String encryptedPassword;

    @OneToOne
    private Major major;

    private String phoneNumber;

    private String code;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<Connect> connects;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<Diagnosis> diagnoses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<Reservation> reservations;
}
