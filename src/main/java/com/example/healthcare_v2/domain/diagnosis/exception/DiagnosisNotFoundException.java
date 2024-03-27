package com.example.healthcare_v2.domain.diagnosis.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class DiagnosisNotFoundException extends ApplicationException {
    public DiagnosisNotFoundException() {
        super(ErrorCode.DIAGNOSIS_NOT_FOUND);
    }
}
