package com.laowang.logindemo.data;

import android.util.Log;

import com.laowang.logindemo.data.model.LoggedInUser;
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

    private RestfulApiHandler restfulApiHandler = new RestfulApiHandler();
    private String apiBase = "http://218.17.142.129:9000/token-browser-backend-0.0.1-SNAPSHOT";
    private String api = "/login";

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            String strUrl = apiBase + api;
            Map<String,String> params = new HashMap<>();
            params.put("username",username);
            params.put("password",password);
            Result<Response>[] result = restfulApiHandler.postSync(null, strUrl, params);
            System.out.println("HelloWorld--------------------------------------------------------"+result[0]);
            /*
            OK,可以测通并返回数据
            restfulApiHandler.getAsync(null,"https://www.httpbin.org/get?a=1&b=2");
            restfulApiHandler.getSync(null,"https://www.httpbin.org/get?a=1&b=2");
            Map<String,String> params = new HashMap<>();
            params.put("a","b");
            params.put("0001","123");
            restfulApiHandler.postAsync(null,"https://www.httpbin.org/post",params);
            restfulApiHandler.postSync(null,"https://www.httpbin.org/post",params);*/
            /*
            咱也不知道为啥连不上
            Log.i("url",strUrl);
            HttpURLConnection conn = restfulApiHandler.getHttpUrlConnection(strUrl);
            conn.setRequestProperty("username",username);
            conn.setRequestProperty("password",password);
            conn.setRequestMethod("POST");
            conn.connect();
            Log.i("响应消息",conn.getResponseMessage());
            Log.i("响应类型",conn.getContentType());
            Log.i("响应内容",conn.getContent().toString());
            Log.i("响应内容长度",conn.getContentLength()+"");
            Log.i("响应内容编码方式",conn.getContentEncoding());
            conn.disconnect();*/
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}