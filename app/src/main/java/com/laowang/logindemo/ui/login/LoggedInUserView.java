package com.laowang.logindemo.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 * 回显 用户名词，vo
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}