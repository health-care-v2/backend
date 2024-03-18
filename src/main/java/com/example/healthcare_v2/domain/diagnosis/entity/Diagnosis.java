package com.example.healthcare_v2.domain.diagnosis.entity;

import com.example.healthcare_v2.domain.doctor.entity.Doctor;
import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.domain.prescription.entity.Prescription;
import com.example.healthcare_v2.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Diagnosis extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String disease;

    @Column(length = 300)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "diagnosis_id")
    private List<DiagnosisImage> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "diagnosis")
    private List<Prescription> prescriptions = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private Diagnosis(String disease, String content, Doctor doctor, Patient patient) {
        this.disease = disease;
        this.content = content;
        this.doctor = doctor;
        this.patient = patient;
    }

    public static Diagnosis of(String disease, String content, Doctor doctor, Patient patient) {
        return new Diagnosis(disease, content, doctor, patient);
    }

    public void changeDiagnosis(String disease, String content, Patient patient, Doctor doctor) {
        this.disease = disease;
        this.content = content;
        this.patient = patient;
        this.doctor = doctor;
    }
}
