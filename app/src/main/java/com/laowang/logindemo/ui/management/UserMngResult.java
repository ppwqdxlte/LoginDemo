package com.laowang.logindemo.ui.management;

import androidx.annotation.Nullable;

import com.laowang.logindemo.data.model.ManagedUser;

/**
 * 和 表单状态 一样，一经构造创建，就无法修改，要想修改只能换新的对象！
 */
public class UserMngResult {

    @Nullable
    private ManagedUser successUser; // 成功操作的对象
    @Nullable
    private Integer successCode; //成功消息资源码
    @Nullable
    private Integer errorCode; // 错误消息资源码

    public UserMngResult() {
    }

    /** 用于【成功】-【新增】【修改】【删除】用户
     * @param successUser su
     * @param successCode sc
     */
    public UserMngResult(@Nullable ManagedUser successUser,@Nullable Integer successCode) {
        this.successUser = successUser;
        this.successCode = successCode;
        this.errorCode = null;
    }

    /** 用于【失败】操作
     * @param errorCode ec
     */
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
