package com.example.healthcare_v2.domain.diary.entity;

import com.example.healthcare_v2.domain.patient.entity.Patient;
import com.example.healthcare_v2.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Boolean isWalk;

    private Boolean isStretching;

    private String takeMedicine;

    private Float status;   // 0~5 별점 계산

    @Column(length = 500)
    private String content;

    @Column(nullable = false)
    private Boolean isPublic;

    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Builder
    public Diary(String title, Boolean isWalk, Boolean isStretching, String takeMedicine, Float status, String content, Boolean isPublic, Long likeCount, Patient patient) {
        this.title = title;
        this.isWalk = isWalk;
        this.isStretching = isStretching;
        this.takeMedicine = takeMedicine;
        this.status = status;
        this.content = content;
        this.isPublic = isPublic;
        this.likeCount = likeCount;
        this.patient = patient;
    }

    public void update(Diary updateDiary){
        this.title = updateDiary.title;
        this.isWalk = updateDiary.isWalk;
        this.isStretching = updateDiary.isStretching;
        this.takeMedicine = updateDiary.takeMedicine;
        this.status = updateDiary.status;
        this.content = updateDiary.content;
        this.isPublic = updateDiary.isPublic;
        this.likeCount = updateDiary.likeCount;
        this.patient = updateDiary.patient;
    }
}
