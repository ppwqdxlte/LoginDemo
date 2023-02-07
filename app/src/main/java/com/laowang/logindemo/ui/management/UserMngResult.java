package com.laowang.logindemo.ui.management;

import androidx.annotation.Nullable;

import com.laowang.logindemo.data.model.ManagedUser;

public class UserMngResult {

    @Nullable
    private ManagedUser successUser; // 成功操作的对象
    @Nullable
    private Integer errorCode; // 错误消息资源码

    public UserMngResult(@Nullable ManagedUser successUser) {
        this.successUser = successUser;
    }

    public UserMngResult(@Nullable Integer errorCode) {
        this.errorCode = errorCode;
    }

    @Nullable
    public ManagedUser getSuccessUser() {
        return successUser;
    }

    public void setSuccessUser(@Nullable ManagedUser successUser) {
        this.successUser = successUser;
    }

    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(@Nullable Integer errorCode) {
        this.errorCode = errorCode;
    }
}
