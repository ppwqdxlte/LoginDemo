package com.laowang.logindemo.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.ui.MyViewModel;
import com.laowang.logindemo.ui.login.LoginActivity;
import com.laowang.logindemo.ui.management.MngFragment;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RestfulApiHandler {

    private final OkHttpClient okHttpClient = new OkHttpClient();

    public Result<String>[] getSync(View view, String url) {
        final Result<String>[] result = new Result[]{null};
        new Thread(() -> {
            Request request = new Request.Builder().url(url).build();
            Call call = okHttpClient.newCall(request);
            Response response = null;
            try {
                response = call.execute();
                String bodyStr = response.body().string();
                Log.e("get同步请求:", "getSync:" + bodyStr);
                result[0] = new Result.Success<>(bodyStr);
            } catch (IOException e) {
                e.printStackTrace();
                result[0] = new Result.Error(e);
            }
        }).start();
        return result;
    }

    public Result<String>[] getAsync(View view, String url) {
        final Result<String>[] result = new Result[]{null};
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                result[0] = new Result.Error(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String bodyStr = response.body().string();
                    Log.e("get异步请求:", "getAsync:" + bodyStr);
                    result[0] = new Result.Success<>(bodyStr);
                } else {
                    int code = response.code();
                    String msg = response.message();
                    Log.e("有问题的get异步请求", "code: " + code + " ,message: " + msg);
                    result[0] = new Result.Problem<>(msg);
                }
            }
        });
        return result;
    }

    public Result<String>[] postSync(View view, String url, Map<String, String> params) {
        final Result<String>[] result = new Result[]{null};
        new Thread(() -> {
            FormBody.Builder builder = new FormBody.Builder();
            if (params != null) {
                for (String s : params.keySet()) {
                    builder.add(s, params.get(s));
                }
            }
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(url)
                    .post(formBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            try {
                Response response = call.execute();
                String bodyStr = response.body().string();
                Log.e("post同步请求", "postSync:" + bodyStr);
                result[0] = new Result.Success<>(bodyStr);
            } catch (IOException e) {
                e.printStackTrace();
                result[0] = new Result.Error(e);
            }
        }).start();
        return result;
    }

    public void postAsync(MyViewModel myViewModel, String url, Map<String, String> params) {
//        final Result<String>[] result = new Result[]{null};
        FormBody.Builder builder = new FormBody.Builder();
        for (String s : params.keySet()) {
            builder.add(s, params.get(s));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url)
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                myViewModel.setmResultString(new Result.Success<>(e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String bodyStr = response.body().string();
                    Log.e("post异步请求", "postAsync:" + bodyStr);
                    myViewModel.setmResultString(new Result.Success<>(bodyStr));
                } else {
                    int code = response.code();
                    String msg = response.message();
                    Log.e("有问题的post异步请求", " code: " + code);
                    myViewModel.setmResultString(new Result.Problem<>(msg));
                }
            }
        });
    }

}
