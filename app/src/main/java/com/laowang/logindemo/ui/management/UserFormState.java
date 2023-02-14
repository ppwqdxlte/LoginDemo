package com.laowang.logindemo.ui.management;

import androidx.annotation.Nullable;

public class UserFormState {

    private Boolean isDataValid;    // 数据是否合法，以下 的交集
    @Nullable
    private Integer newNameError;   // 新用户名不符合规范【为空 或 长度太长太短】 null 或 1 表示没问题，下述同理
    @Nullable
    private Integer newPwdError;    // 新密码。。      【为空 或 长度太长太短】
    @Nullable
    private Integer repeatPwdError; // 重复密码。。    【为空 或 与新密码不同】
    @Nullable
    private Integer roleError;      // 系统权限角色。。 【未选中任何角色】
    @Nullable
    private Integer oldPwdError;    // 旧密码。。      【旧密码输入错误】
    @Nullable
    private Integer selectedNameError; // 选中用户名。。【未选中用户名】

    public UserFormState() {
    }

    public UserFormState(Boolean isDataValid) {
        this.isDataValid = isDataValid;
        newNameError = null;
        newPwdError = null;
        repeatPwdError = null;
        roleError = null;
        oldPwdError = null;
        selectedNameError = null;
    }

    public UserFormState(@Nullable Integer newNameError, @Nullable Integer newPwdError, @Nullable Integer repeatPwdError) {
        this.newNameError = newNameError;
        this.newPwdError = newPwdError;
        this.repeatPwdError = repeatPwdError;
        roleError = null;
        oldPwdError = null;
        isDataValid = true;
        if (newNameError != null && newNameError != 1
                || newPwdError != null && newPwdError != 1
                || repeatPwdError != null && repeatPwdError != 1) {
            this.isDataValid = false;
        }
    }

    public UserFormState(Boolean isDataValid, @Nullable Integer oldPwdError, @Nullable Integer newPwdError, @Nullable Integer repeatPwdError) {
        this.oldPwdError = oldPwdError;
        this.newPwdError = newPwdError;
        this.repeatPwdError = repeatPwdError;
        newNameError = null;
        roleError = null;
        this.isDataValid = true;
        if (oldPwdError != null && oldPwdError != 1
                || newPwdError != null && newPwdError != 1
                || repeatPwdError != null && repeatPwdError != 1) {
            this.isDataValid = false;
        }
    }

    public UserFormState(@Nullable Integer selectedNameError, @Nullable Integer oldPwdError, @Nullable Integer newPwdError, @Nullable Integer repeatPwdError) {
        this.selectedNameError = selectedNameError;
        this.oldPwdError = oldPwdError;
        this.newPwdError = newPwdError;
        this.repeatPwdError = repeatPwdError;
        newNameError = null;
        roleError = null;
        this.isDataValid = true;
        if (selectedNameError != null && selectedNameError != 1
                || oldPwdError != null && oldPwdError != 1
                || newPwdError != null && newPwdError != 1
                || repeatPwdError != null && repeatPwdError != 1) {
            this.isDataValid = false;
        }
    }

    public UserFormState(@Nullable Integer selectedNameError) {
        this.selectedNameError = selectedNameError;
        this.newNameError = null;
        this.oldPwdError = null;
        this.newPwdError = null;
        this.repeatPwdError = null;
        this.roleError = null;
        this.isDataValid = true;
        if (selectedNameError != null && selectedNameError != 1) {
            this.isDataValid = false;
        }
    }

    @Override
    public String toString() {
        return "UserFormState{" +
                "isDataValid=" + isDataValid +
                ", newNameError=" + newNameError +
                ", newPwdError=" + newPwdError +
                ", repeatPwdError=" + repeatPwdError +
                ", roleError=" + roleError +
                ", oldPwdError=" + oldPwdError +
                ", selectedNameError=" + selectedNameError +
                '}';
    }

    public Boolean getDataValid() {
        return isDataValid;
    }

    @Nullable
    public Integer getNewNameError() {
        return newNameError;
    }

    @Nullable
    public Integer getNewPwdError() {
        return newPwdError;
    }

    @Nullable
    public Integer getRepeatPwdError() {
        return repeatPwdError;
    }

    @Nullable
    public Integer getRoleError() {
        return roleError;
    }

    @Nullable
    public Integer getOldPwdError() {
        return oldPwdError;
    }

    @Nullable
    public Integer getSelectedNameError() {
        return selectedNameError;
    }
}
