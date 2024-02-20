package com.example.healthcare_v2.domain.patient.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class UserNotFoundException extends ApplicationException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
