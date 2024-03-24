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
import java.util.List;
import java.util.Objects;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Doctor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String encryptedPassword;

    private String name;

    private String major;

    private String phoneNumber;

    private String code;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<Connect> connects;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<Diagnosis> diagnoses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "doctor")
    private List<Reservation> reservations;

    @Builder
    public Doctor(String email, String encryptedPassword, String name, String major,
        String phoneNumber,
        String code, List<Connect> connects, List<Diagnosis> diagnoses,
        List<Reservation> reservations) {
        this.email = email;
        this.encryptedPassword = encryptedPassword;
        this.name = name;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.connects = connects;
        this.diagnoses = diagnoses;
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
