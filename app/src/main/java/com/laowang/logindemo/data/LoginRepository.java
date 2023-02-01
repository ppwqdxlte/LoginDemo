package com.laowang.logindemo.data;

import android.app.Activity;

import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.ui.login.LoginActivity;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 * 该类：从远程数据源请求身份验证和用户信息，并维护登录状态和用户凭据信息的内存缓存。
 */
public class LoginRepository {
    /**
     * 静态成员实例，线程可见
     */
    private static volatile LoginRepository instance;
    /**
     * 数据源
     */
    private LoginDataSource dataSource;
    /**
     * 保存当前登录的用户信息
     * If user credentials will be cached in local storage, it is recommended it be encrypted
     */
    private LoggedInUser user = null;

    /**
     * private constructor : singleton access 单粒模式
     *
     * @param dataSource 数据源
     */
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 单粒意味着 dataSource数据源一经确定就无法改变，且整个应用只有一个LoginRepository对象
     * 【疑问】不合理之处在于APP需要支持并发登录，可能多人同时在线，旧代码一个repository“仓库”只有一个user,之后的更新中需要升级成users集合，第二种方法：取消LoginRepository的单粒模式，一个repository干脆对应一个user
     * 【反驳】上述疑问显然想太多了！不存在并发问题，因为这是APP代码只跑在单个手机上！又不是跑在Server上！当然一个手机对应一个登录用户啦！！
     *
     * @param dataSource 数据源？Jetpack框架形式的数据源
     * @return 单粒的LoginRepository对象
     */
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

    /**
     * 设置登录对象
     *
     * @param user 成功登录的用户
     */
    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    /**
     * 登录,调用dataSource数据源的login方法，如果成功则设置当前登录用户
     *
     * @param username username
     * @param password password
     * @return Result /data/.. 登录结果
     */
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