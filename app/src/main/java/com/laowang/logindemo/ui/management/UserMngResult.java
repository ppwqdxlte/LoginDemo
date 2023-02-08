package com.laowang.logindemo.ui.management;

import androidx.annotation.Nullable;

import com.laowang.logindemo.data.model.ManagedUser;

public class UserMngResult {

    @Nullable
    private ManagedUser successUser; // 成功操作的对象
    @Nullable
    private Integer successCode; //成功消息资源码
    @Nullable
    private Integer errorCode; // 错误消息资源码

    public UserMngResult(@Nullable ManagedUser successUser) {
        this.successUser = successUser;
    }

    public UserMngResult(@Nullable Integer successCode, @Nullable Integer errorCode) {
        this.successCode = successCode;
        this.errorCode = errorCode;
    }

    public UserMngResult() {
    }

    @Nullable
    public ManagedUser getSuccessUser() {
        return successUser;
    }

    public void setSuccessUser(@Nullable ManagedUser successUser) {
        this.successUser = successUser;
    }

    @Nullable
    public Integer getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(@Nullable Integer successCode) {
        this.successCode = successCode;
    }

    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(@Nullable Integer errorCode) {
        this.errorCode = errorCode;
    }
}
