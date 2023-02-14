package com.laowang.logindemo.ui.token;

import androidx.annotation.Nullable;

public class TokenResult {

    @Nullable
    private Integer successCode;

    @Nullable
    private Integer errorCode;
    @Nullable
    private Integer successKctCode;
    @Nullable
    private Integer errorKctCode;
    @Nullable
    private Integer successTccCode;
    @Nullable
    private Integer errorTccCode;

    public TokenResult(@Nullable Integer successCode, @Nullable Integer errorCode) {
        this.successCode = successCode;
        this.errorCode = errorCode;
    }

    public TokenResult(@Nullable Integer successKctCode, @Nullable Integer errorKctCode, @Nullable Integer successTccCode, @Nullable Integer errorTccCode) {
        this.successKctCode = successKctCode;
        this.errorKctCode = errorKctCode;
        this.successTccCode = successTccCode;
        this.errorTccCode = errorTccCode;
        if (successKctCode != null) {
            this.successCode = successKctCode;
        }
        if (errorKctCode != null) {
            this.errorCode = errorKctCode;
        }
        if (successTccCode != null) {
            this.successCode = successTccCode;
        }
        if (errorTccCode != null) {
            this.errorCode = errorTccCode;
        }
    }

    @Nullable
    public Integer getSuccessCode() {
        return successCode;
    }

    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }

    @Nullable
    public Integer getSuccessKctCode() {
        return successKctCode;
    }

    @Nullable
    public Integer getErrorKctCode() {
        return errorKctCode;
    }

    @Nullable
    public Integer getSuccessTccCode() {
        return successTccCode;
    }

    @Nullable
    public Integer getErrorTccCode() {
        return errorTccCode;
    }
}
