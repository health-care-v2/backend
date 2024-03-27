package com.example.healthcare_v2.domain.reservation.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class ReservationPatientNotMatchException extends ApplicationException {
    public ReservationPatientNotMatchException() {
        super(ErrorCode.RESERVATION_PATIENT_NOT_MATCH);
    }
}
