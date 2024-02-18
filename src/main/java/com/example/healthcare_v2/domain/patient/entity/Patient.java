package com.example.healthcare_v2.domain.patient.entity;

import com.example.healthcare_v2.domain.connect.entity.Connect;
import com.example.healthcare_v2.domain.diagnosis.entity.Diagnosis;
import com.example.healthcare_v2.domain.diary.entity.Diary;
import com.example.healthcare_v2.domain.reservation.entity.Reservation;
import com.example.healthcare_v2.global.common.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Entity
@Getter
public class Patient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, unique = true)
    private String email;

    private String encryptedPassword;

    private String name;

    private LocalDate born;

    @Column(unique = true)
    private String phoneNumber;

    private String addr1;

    private String addr2;

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,mappedBy = "patient")
    private List<Connect> connects;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "patient")
    private List<Diary> diaries;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "patient")
    private List<Reservation> reservations;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "patient")
    private List<Diagnosis> Diagnoses;
}
