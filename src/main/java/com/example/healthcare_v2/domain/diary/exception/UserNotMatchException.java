package com.example.healthcare_v2.domain.diary.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class UserNotMatchException extends ApplicationException {
    public UserNotMatchException() {
        super(ErrorCode.USER_NOT_MATCH);
    }
}
