package com.laowang.logindemo.ui.management;

import androidx.annotation.Nullable;

/**
 * 用户有关操作的表单状态【通用】，不同操作有不同的表单，组合操作即可
 * 部件消息提示2大基本原则：
 * 1.鼠标【失去焦点】时不符合规范就会提醒，失去焦点分2种情况【null 或 长度不符】，符合规范的话就不会提醒！
 * 2.输入完表单内最后一个非按钮部件后，判断isDataValid(),ture并且显示的input都不为空（这里需要ViewModel内逻辑判断）！
 * 则激活 CONFIRM或DELETE按钮，灰色变红色；否则一直灰色，且从上往下第一个不规范的form部件变成选中状态！
 * <p>
 * 删掉所有setter,只能构造函数创建新的状态，一经创建就不允许修改，只能换新的！
 */
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

    /**
     * 主要用于【清空表单状态】
     *
     * @param isDataValid true-清空状态且激活提交按钮，false-清空状态且睡眠提交按钮
     */
    public UserFormState(Boolean isDataValid) {
        this.isDataValid = isDataValid;
        newNameError = null;
        newPwdError = null;
        repeatPwdError = null;
        roleError = null;
        oldPwdError = null;
        selectedNameError = null;
    }

    /**
     * 用于【新增用户】表单
     *
     * @param newNameError   ne
     * @param newPwdError    npe
     * @param repeatPwdError rpe
     */
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

    /**
     * 用于【修改密码】表单
     *
     * @param isDataValid    idv
     * @param newPwdError    npe
     * @param repeatPwdError rpe
     * @param oldPwdError    ope
     */
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

    /**
     * 用于【修改用户】表单
     *
     * @param selectedNameError sne
     * @param newPwdError       npe
     * @param repeatPwdError    rpe
     * @param oldPwdError       ope
     */
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

    /**
     * 用于【删除用户】表单
     *
     * @param selectedNameError sne
     */
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
