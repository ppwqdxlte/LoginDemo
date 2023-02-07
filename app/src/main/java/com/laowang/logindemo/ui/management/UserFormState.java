package com.laowang.logindemo.ui.management;

import androidx.annotation.Nullable;

/**
 * 用户有关操作的表单状态【通用】，不同操作有不同的表单，组合操作即可
 * 部件消息提示2大基本原则：
 * 1.鼠标【失去焦点】时不符合规范就会提醒，失去焦点分2种情况【null 或 长度不符】，符合规范的话就不会提醒！
 * 2.输入完表单内最后一个非按钮部件后，判断isDataValid(),ture并且显示的input都不为空（这里需要ViewModel内逻辑判断）！
 *  则激活 CONFIRM或DELETE按钮，灰色变红色；否则一直灰色，且从上往下第一个不规范的form部件变成选中状态！
 */
public class UserFormState {

    private Boolean isDataValid;    // 数据是否合法，以下 的交集
    @Nullable
    private Integer newNameError;   // 新用户名不符合规范【为空 或 长度太长太短】
    @Nullable
    private Integer newPwdError;    // 新密码。。      【为空 或 长度太长太短】
    @Nullable
    private Integer repeatPwdError; // 重复密码。。    【为空 或 与新密码不同】
    @Nullable
    private Integer roleError;      // 系统权限角色。。 【未选中任何角色】
    @Nullable
    private Integer oldPwdError;    // 旧密码。。      【旧密码输入错误】

    @Override
    public String toString() {
        return "UserFormState{" +
                "isDataValid=" + isDataValid +
                ", newNameError=" + newNameError +
                ", newPwdError=" + newPwdError +
                ", repeatPwdError=" + repeatPwdError +
                ", roleError=" + roleError +
                ", oldPwdError=" + oldPwdError +
                '}';
    }

    public Boolean getDataValid() {
        if (newNameError != null || newPwdError != null || repeatPwdError != null ||
                oldPwdError != null || roleError != null) {
            isDataValid = false;
        } else {
            isDataValid = true;
        }
        return isDataValid;
    }

    public Integer getNewNameError() {
        return newNameError;
    }

    public UserFormState setNewNameError(Integer newNameError) {
        this.newNameError = newNameError;
        return this;
    }

    public Integer getNewPwdError() {
        return newPwdError;
    }

    public UserFormState setNewPwdError(Integer newPwdError) {
        this.newPwdError = newPwdError;
        return this;
    }

    public Integer getRepeatPwdError() {
        return repeatPwdError;
    }

    public UserFormState setRepeatPwdError(Integer repeatPwdError) {
        this.repeatPwdError = repeatPwdError;
        return this;
    }

    public Integer getRoleError() {
        return roleError;
    }

    public UserFormState setRoleError(Integer roleError) {
        this.roleError = roleError;
        return this;
    }

    public Integer getOldPwdError() {
        return oldPwdError;
    }

    public UserFormState setOldPwdError(Integer oldPwdError) {
        this.oldPwdError = oldPwdError;
        return this;
    }
}
