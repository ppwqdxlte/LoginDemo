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

    public UserMngResult() {
    }

    public UserMngResult(@Nullable ManagedUser successUser,@Nullable Integer successCode) {
        this.successUser = successUser;
        this.successCode = successCode;
        this.errorCode = null;
    }

    public UserMngResult(@Nullable Integer errorCode) {
        this.successUser = null;
        this.successCode = null;
        this.errorCode = errorCode;
    }

    @Nullable
    public ManagedUser getSuccessUser() {
        return successUser;
    }

    @Nullable
    public Integer getSuccessCode() {
        return successCode;
    }

    @Nullable
    public Integer getErrorCode() {
        return errorCode;
    }
}
