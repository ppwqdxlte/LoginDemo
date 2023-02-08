package com.laowang.logindemo.data;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.R;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.data.model.ManagedUser;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 用户管理页的数据源.
 */
public class MngDataSource {
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

    public Result<ManagedUser> createUser(String newUsername, String newPwd, int checkedRadioButtonId) {
        ManagedUser managedUser = null;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_CREATE_USER;
        Map<String,String> params = new HashMap<>();
        params.put("username",newUsername);
        params.put("password",newPwd);
        params.put("permisstionIndex",checkedRadioButtonId+"");
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success){
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null){
                return new Result.Error("Fail to create user.", R.string.result_fail_create_user);
            }
            LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
            managedUser = new ManagedUser( treeMap.get("username").toString(), treeMap.get("password").toString(), treeMap.get("level").toString());
            managedUser.setDate(treeMap.get("date").toString());
            return new Result.Success<ManagedUser>(managedUser,"Create user successfully!",R.string.result_success_create_user);
        } else if (result[0] instanceof Result.Error){
            return (Result.Error)result[0];
        } else {
            return (Result.Problem)result[0];
        }
    }
}
