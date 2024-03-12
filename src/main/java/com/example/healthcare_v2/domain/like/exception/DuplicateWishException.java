package com.example.healthcare_v2.domain.like.exception;

import com.example.healthcare_v2.global.exception.ApplicationException;
import com.example.healthcare_v2.global.exception.ErrorCode;

public class DuplicateWishException extends ApplicationException {
    public DuplicateWishException() {
        super(ErrorCode.DUPLICATED_WISH);
    }
}
