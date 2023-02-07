package com.laowang.logindemo.data.model;

public class ManagedUser {

    private String username;
    private String password;
    private String roleCode; //角色码 0-普通用户 1-管理员
    private String date;

    public ManagedUser(String username, String password, String roleCode) {
        this.username = username;
        this.password = password;
        this.roleCode = roleCode;
    }

    public ManagedUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "ManagedUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleCode='" + roleCode + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
