package com.yetx.exception;

import com.yetx.common.ResultStatus;

public class GlobalException extends RuntimeException {
    private ResultStatus status;

    public GlobalException(ResultStatus status) {
        super(status.toString());
        this.status = status;
    }

    public ResultStatus getResultStatus() {
        return status;
    }
}
