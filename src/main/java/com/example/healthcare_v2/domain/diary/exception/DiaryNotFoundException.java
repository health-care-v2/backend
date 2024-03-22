package com.example.healthcare_v2.domain.diary.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class DiaryNotFoundException extends ApplicationException {
    public DiaryNotFoundException() {
        super(ErrorCode.DIARY_NOT_FOUND);
    }
}
