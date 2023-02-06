package com.laowang.logindemo.ui.management.result;

import androidx.annotation.Nullable;

import com.laowang.logindemo.ui.login.LoggedInUserView;

public class CreateResult {

    @Nullable
    private LoggedInUserView success;

    @Nullable
    private Integer error;

    public CreateResult(LoggedInUserView success) {
        this.success = success;
    }

    public CreateResult(int errorRes){
        this.error = errorRes;
    }

    @Nullable
    public Integer getError() {
        return error;
    }

    @Nullable
    public LoggedInUserView getSuccess() {
        return success;
    }
}
