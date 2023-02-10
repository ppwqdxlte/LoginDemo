package com.laowang.logindemo.data;

public class ApiUrl {
    /**
     * 数据源的 根URL
     */
    public static String API_BASE = "http://218.17.142.129:9000/token-browser-backend-0.0.1-SNAPSHOT";
    /**
     * API后缀 登录
     */
    public static String API_LOGIN = "/login";
    /**
     * API后缀 查询所有用户用户
     */
    public static String API_USER_LIST = "/queryUserList";
    /**
     * API后缀 修改用户密码
     */
    public static String API_PASSWORD = "/changePassword";
    /**
     * API后缀 新增用户
     */
    public static final String API_CREATE_USER = "/createUser";
    /**
     * API后缀 删除用户
     */
    public static final String API_DELETE_USER = "/deleteUser";
    /**
     * API后缀 通过Token类型和表号查询tokens
     */
    public static final String API_TYPE_METER = "/queryTokenListbyMeterStr";
}
