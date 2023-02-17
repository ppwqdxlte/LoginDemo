package com.laowang.logindemo.data;

import android.app.Activity;

import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.ui.login.LoginActivity;

/**
 * Class that requests authentication and user information from the remote data source and
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    private LoggedInUser user = null;

    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }
    /**
     * @return 是否登录
     */
    public boolean isLoggedIn() {
        return user != null;
    }

    public LoggedInUser getUser() {
        return user;
    }

    /**
     * 登出，移除user对象(√)，数据源关闭连接
     */
    public void logout(Activity from, Class<LoginActivity> dest) {
        user = null;
        dataSource.logout(from, dest);
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            // 如果登录成功，需要设置登录对象
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}