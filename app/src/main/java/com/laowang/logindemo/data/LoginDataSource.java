package com.laowang.logindemo.data;

import android.util.Log;

import com.google.gson.Gson;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.databinding.ActivityLoginBinding;
import com.laowang.logindemo.ui.login.LoginViewModel;
import com.laowang.logindemo.ui.login.LoginViewModelFactory;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.io.IOException;
import java.lang.invoke.MethodType;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    /**
     * REST接口通用访问工具
     */
    private RestfulApiHandler restfulApiHandler = new RestfulApiHandler();
    /**
     * 数据源的 根URL
     */
    private String apiBase = "http://218.17.142.129:9000/token-browser-backend-0.0.1-SNAPSHOT";
    /**
     * API后缀
     */
    private String api = "/login";

    /** 登录方法的源头
     * @param username username
     * @param password password
     * @return Result对象 /data/.. 携带登录用户结果
     */
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // : handle loggedInUser authentication
            String strUrl = apiBase + api;
            Map<String,String> params = new HashMap<>();
            params.put("username",username);
            params.put("password",password);
            Result<Response>[] result = restfulApiHandler.postSync(null, strUrl, params);
            System.out.println("HelloWorld--------------------------------------------------------"+result[0]);
            /*等待结果不为空*/
            while (result[0] == null){}
            if (result[0] instanceof Result.Success){
                // TODO: 假用户，登录成功需要返回真实用户
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                java.util.UUID.randomUUID().toString(),
                                "Jane Doe");
                Response res = ((Result.Success<Response>) result[0]).getData();
                String jsonStr = res.body().string();
                // TODO json字符串转换成对象
                return new Result.Success<>(fakeUser);
            }
            throw new Exception("压根儿访问不到");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    /**
     * 登出，调用此法的上游方法已经移除登录user，这里只要跳转到Login页面即可
     */
    public void logout() {
        // TODO: 并跳转到LoginActivity登录页面
        //  TODO (1)关闭当前 MainActivity
        //  TODO (2)打开LoginActivity
    }
}