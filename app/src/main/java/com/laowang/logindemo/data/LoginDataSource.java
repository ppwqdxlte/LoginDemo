package com.laowang.logindemo.data;

import android.app.Activity;
import android.content.Intent;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.ui.login.LoginActivity;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    /**
     * REST接口通用访问工具
     */
    private RestfulApiHandler apiHandler = new RestfulApiHandler();
    /**
     * 登录方法的源头
     *
     * @param username username
     * @param password password
     * @return Result对象 /data/.. 携带登录用户结果
     */
    public Result<LoggedInUser> login(String username, String password) {
        try {
            // : handle loggedInUser authentication
            String strUrl = ApiUrl.API_BASE + ApiUrl.API_LOGIN;
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            Result<String>[] result = apiHandler.postSync(null, strUrl, params);
            // 直到result[0]不为空退出循环执行后面方法
            while (result[0] == null) {
            }
            if (result[0] instanceof Result.Success) {
                Result.Success<String> success = (Result.Success<String>) result[0];
                String jsonStr = success.getData();
                // eg.用户或密码错误时候，{"data":null,"msg":null,"code":null,"throwable":null}
//                Log.e("body to string", jsonStr);
                com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
                // 需注意 空指针异常
                if (fromJson.getData() == null) {
                    return new Result.Error("Error username or password~", new Exception("No such an account exists!"));
                }
//                Log.i("Gson's Data toString", fromJson.getData().toString());
                LinkedTreeMap data = (LinkedTreeMap) fromJson.getData();
                return new Result.Success<>(new LoggedInUser(
                        java.util.UUID.randomUUID().toString(),
                        data.get("username").toString(),
                        data.get("password").toString(),
                        data.get("level").toString(),
                        data.get("date").toString()), null);
            } else {
                throw new Exception("访问API失败！");
            }
        } catch (NetworkOnMainThreadException e) {
            Log.w("System.err", "主线程网络请求错误,再请求一次，基本问题不大。");
            return new Result.Problem(e, "不凑是主线程不该有网络请求吗？问题不大", 500);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    /**
     * 登出，调用此法的上游方法已经移除登录user，这里只要从 主页MainActivity 跳转到Login页面即可
     */
    public void logout(Activity from, Class<LoginActivity> dest) {
        // 跳转到LoginActivity登录页面 (1)关闭当前 MainActivity (2)打开LoginActivity
        from.finish();
        Intent loginIntent = new Intent(from, dest);
        from.startActivity(loginIntent);
    }
}