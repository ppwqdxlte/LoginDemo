package com.laowang.logindemo.util;

import android.app.DownloadManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.laowang.logindemo.data.Result;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 自己写的REST接口通用访问工具：
 * 同步GET，异步GET，同步POST，异步POST
 */
public class RestfulApiHandler {

    private OkHttpClient okHttpClient = new OkHttpClient();

    /**
     * GET同步请求【注意网络同步请求原则上要有一个子线程，
     * 【反转】操他腚的，我说怎么得不到数据，我主线程都跑完了，子线程还没跑完操你腚的那还开个JB的新线程，同步阻塞算了！
     * 【再反转】安卓规定不能在主线程里面直接搞同步网络请求，必须开子线程才行，你头铁的话就报错：W/System.err: android.os.NetworkOnMainThreadException】
     * GET同步请求【注意网络同步请求原则上要有一个子线程，【反转】操他腚的，我说怎么得不到数据，我主线程都跑完了，子线程还没跑完操你腚的
     * 那还开个JB的新线程，同步阻塞算了！】
     *
     * @param view 继承View的实现类对象
     * @param url  get方法URL后边可以跟着参数 比如：“https://www.httpbin.org/get?a=1&b=2”
     *             https://www.httpbin.org/get为开源测试网址
     * @return response[] 单元素响应数组对象，实际只有0号索引有用，
     * 【注意】为啥整个数组都要返回呢？因为异步的性质，可能在return的时候新线程还没产生res[0]的数据，
     * 所以返回数组的引用，方便追踪到res[0]的值，左侧注意为放P，不懂瞎说的，再次特意指正一下
     */
    public Result<String>[] getSync(View view, String url) {
        final Result<String>[] result = new Result[]{null};
        new Thread(() -> {
            Request request = new Request.Builder().url(url).build();
            //请求的call对象
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

    /**
     * GET异步请求【异步不需要创建线程】
     *
     * @param view 继承View的实现类对象
     * @param url  eg. "https://www.httpbin.org/get?a=1&b=2"
     */
    public Result<String>[] getAsync(View view, String url) {
        final Result<String>[] result = new Result[]{null};
        Request request = new Request.Builder().url(url).build();
        //请求的call对象
        Call call = okHttpClient.newCall(request);
        //异步请求
        call.enqueue(new Callback() {
            //失败的请求
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                result[0] = new Result.Error(e);
            }

            //结束的回调
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                //响应码可能是404也可能是200都会走这个方法
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

    /**
     * okp默认是get请求,post需要有请求体，即formBody
     *
     * @param view   继承View的实现类对象
     * @param url    eg. "https://www.httpbin.org/post"
     * @param params Map<String,String> 字符串参数map
     * @return Result<Response>[] single-element result
     */
    public Result<String>[] postSync(View view, String url, Map<String, String> params) {
        final Result<String>[] result = new Result[]{null};
        new Thread(() -> {
            // FormBody是RequestBody的子类
            FormBody.Builder builder = new FormBody.Builder();
            // 注意params要做非空判断
            if (params != null) {
                for (String s : params.keySet()) {
                    builder.add(s, params.get(s));
                }
            }
            FormBody formBody = builder.build();
            Request request = new Request.Builder().url(url)
                    .post(formBody)
                    .build();
            //请求的call对象
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

    /**
     * @param view   sub-class object
     * @param url    eg. "https://www.httpbin.org/post"
     * @param params Map<String,String> request parameters
     */
    public Result<String>[] postAsync(View view, String url, Map<String, String> params) {
        final Result<String>[] result = new Result[]{null};
        FormBody.Builder builder = new FormBody.Builder();
        for (String s : params.keySet()) {
            builder.add(s, params.get(s));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url)
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                result[0] = new Result.Success<>(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String bodyStr = response.body().string();
                    Log.e("post异步请求", "postAsync:" + bodyStr);
                    result[0] = new Result.Success<>(bodyStr);
                } else {
                    int code = response.code();
                    String msg = response.message();
                    Log.e("有问题的post异步请求", " code: " + code);
                    result[0] = new Result.Problem<>(msg);
                }
            }
        });
        return result;
    }

}
