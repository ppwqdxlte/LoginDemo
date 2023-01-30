package com.laowang.logindemo.data;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.apientity.TokenUser;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.databinding.ActivityLoginBinding;
import com.laowang.logindemo.ui.login.LoginViewModel;
import com.laowang.logindemo.ui.login.LoginViewModelFactory;
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
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
            // 直到result[0]不为空退出循环执行后面方法
            while (result[0] == null) {}
            if (result[0] instanceof Result.Success) {
                /*Result.Success<Response> success = (Result.Success<Response>) result[0];
                Response response = success.getData();*/
                // 注意，response.body().string()用一次就关闭！！所以用一个变量保存好！
                // 最好还是子线程调用此方法，因为哪怕主线程使用了response，也算在主线程使用网络，还是会间歇性抽风报错，
                // 而且，你看response对象除了提供 body string，还有嘛用途 啊，干脆重构api方法返回json string算了！
                Result.Success<String> success = (Result.Success<String>) result[0];
                String jsonStr = success.getData();
                // eg.用户或密码错误时候，{"data":null,"msg":null,"code":null,"throwable":null}
                Log.e("body to string", jsonStr);
                com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
                // 需注意 空指针异常
                if (fromJson.getData() == null) {
                    return new Result.Error("Error username or password~", new Exception("No such an account exists!"));
                }
                Log.i("Gson's Data toString", fromJson.getData().toString());
                LinkedTreeMap data = (LinkedTreeMap) fromJson.getData();
                return new Result.Success<>(new LoggedInUser(java.util.UUID.randomUUID().toString(), data.get("username").toString()),null);
            } else {
                throw new Exception("访问API失败！");
            }
        } catch (NetworkOnMainThreadException e){
            Log.w("System.err","主线程网络请求错误,再请求一次，基本问题不大。");
            return new Result.Problem(e,"不凑是主线程不该有网络请求吗？问题不大",500);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    /**
     * 登出，调用此法的上游方法已经移除登录user，这里只要跳转到Login页面即可
     */
    public void logout() {
        // TODO: 并跳转到LoginActivity登录页面 (1)关闭当前 MainActivity (2)打开LoginActivity
    }
}