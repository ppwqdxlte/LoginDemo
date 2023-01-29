package com.laowang.logindemo.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.apientity.TokenUser;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodType;
import java.net.HttpURLConnection;
import java.nio.CharBuffer;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;

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
            // 直到result[0]不为空退出循环执行后面方法
            while (result[0] == null){}
            Log.i("返回结果","HelloWorld--------------------------------------------------------"+result[0]);
            if (result[0] instanceof Result.Success){
                Result.Success<Response> success = (Result.Success<Response>)result[0];
                Response response = success.getData();
                //注意，response.body().string()用一次就关闭！！所以用一个变量保存好！
                String string = response.body().string();
                Log.e("body to string",string);
                com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(string, com.laowang.logindemo.apientity.Result.class);
                Log.i("Gson's Data toString",fromJson.getData().toString());
                LinkedTreeMap data = (LinkedTreeMap) fromJson.getData();
                return new Result.Success<>(new LoggedInUser(java.util.UUID.randomUUID().toString(),data.get("username").toString())
                        ,"Successfully logged in!!!");
            }
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
            else {
                return new Result.Problem<>("Error username or password!",null);
            }
            /*LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);*/
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}