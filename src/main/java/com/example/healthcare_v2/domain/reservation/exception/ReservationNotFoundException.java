package com.example.healthcare_v2.domain.reservation.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class ReservationNotFoundException extends ApplicationException {
    public ReservationNotFoundException() {
        super(ErrorCode.RESERVATION_NOT_FOUND);
    }
}
