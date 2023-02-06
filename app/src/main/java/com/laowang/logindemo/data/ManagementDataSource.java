package com.laowang.logindemo.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户管理页的数据源.
 */
public class ManagementDataSource {
    /**
     * REST接口通用访问工具
     */
    private RestfulApiHandler restfulApiHandler = new RestfulApiHandler();

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    public List<LoggedInUser> queryUserList() {
        List<LoggedInUser> users = new ArrayList<>();
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_USER_LIST;
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, null);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success){
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null){
                return users;
            }
            // 列表的话是List<LinkedTreeMap>类型
            List<LinkedTreeMap> data = (ArrayList<LinkedTreeMap>) fromJson.getData();
            for (int i = 0; i < data.size(); i++) {
                LinkedTreeMap treeMap = data.get(i);
                LoggedInUser user = new LoggedInUser(UUID.randomUUID().toString(),
                        treeMap.get("username").toString(),
                        treeMap.get("password").toString(),
                        treeMap.get("level").toString(),
                        treeMap.get("date").toString());
                users.add(user);
            }
        }
        return users;
    }

    public LoggedInUser create(String username, String pwd1, int checkedRadioButtonId) {
        LoggedInUser loggedInUser = null;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_CREATE_USER;
        Map<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",pwd1);
        params.put("permisstionIndex",checkedRadioButtonId+"");
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success){
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null){
                return loggedInUser;
            }
            LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
            loggedInUser = new LoggedInUser(UUID.randomUUID().toString(),
                    treeMap.get("username").toString(),
                    treeMap.get("password").toString(),
                    treeMap.get("level").toString(),
                    treeMap.get("date").toString());
        }
        return loggedInUser;
    }
}
