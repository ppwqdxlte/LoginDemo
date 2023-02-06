package com.laowang.logindemo.ui.management.formstate;

import androidx.annotation.Nullable;

public class CreateFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer pwdError;
    @Nullable
    private Integer notEqualError;
    @Nullable
    private Integer permissionRoleError;

    private boolean isDataValid;

    public CreateFormState(@Nullable Integer usernameError, @Nullable Integer pwdError,@Nullable Integer notEqualError,@Nullable Integer permissionRoleError) {
        this.usernameError = usernameError;
        this.pwdError = pwdError;
        this.notEqualError = notEqualError;
        this.permissionRoleError = permissionRoleError;
        this.isDataValid = false;
    }

    public CreateFormState(boolean isDataValid) {
        this.usernameError = null;
        this.pwdError = null;
        this.notEqualError = null;
        this.permissionRoleError = null;
        this.isDataValid = isDataValid;
    }

    public boolean isDataValid() {
        return this.isDataValid;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    public Integer getPermissionRoleError() {
        return permissionRoleError;
    }

    @Nullable
    public Integer getPwdError() {
        return pwdError;
    }

    @Nullable
    public Integer getNotEqualError() {
        return notEqualError;
    }
}
