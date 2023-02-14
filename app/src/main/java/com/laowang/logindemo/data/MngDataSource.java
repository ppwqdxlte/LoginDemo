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

public class MngDataSource {

    private RestfulApiHandler restfulApiHandler = new RestfulApiHandler();

    public List<LoggedInUser> queryUserList() {
        List<LoggedInUser> users = new ArrayList<>();
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_USER_LIST;
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, null);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
                return users;
            }
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
        Map<String, String> params = new HashMap<>();
        params.put("username", newUsername);
        params.put("password", newPwd);
        params.put("permisstionIndex", checkedRadioButtonId + "");
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
                return new Result.Error("Fail to create user.", R.string.result_fail_create_user);
            }
            LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
            managedUser = new ManagedUser(treeMap.get("username").toString(), treeMap.get("password").toString(), treeMap.get("level").toString());
            managedUser.setDate(treeMap.get("date").toString());
            return new Result.Success<ManagedUser>(managedUser, "Create user successfully!", R.string.result_success_create_user);
        } else if (result[0] instanceof Result.Error) {
            return (Result.Error) result[0];
        } else {
            return (Result.Problem) result[0];
        }
    }

    public Result<ManagedUser> deleteUser(String selectedName) {
        ManagedUser managedUser = null;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_DELETE_USER;
        Map<String, String> params = new HashMap<>();
        params.put("selectedUser", selectedName);
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
                return new Result.Error("Fail to delete user.", R.string.result_fail_delete_user);
            }
            LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
            managedUser = new ManagedUser(treeMap.get("username").toString(), null, null);
            return new Result.Success<ManagedUser>(managedUser, "Delete user successfully!", R.string.result_success_delete_user);
        } else if (result[0] instanceof Result.Error) {
            return (Result.Error) result[0];
        } else {
            return (Result.Problem) result[0];
        }
    }

    public Result<ManagedUser> modifyUser(String selectedName, String oldPwd, String newPwd, String repeatPwd) {
        ManagedUser managedUser = null;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_PASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("username", selectedName);
        params.put("oldPwd", oldPwd);
        params.put("newPwd01", newPwd);
        params.put("newPwd02", repeatPwd);
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
                return new Result.Error("Fail to modify user.", R.string.result_fail_modify_user);
            }
            LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
            managedUser = new ManagedUser(treeMap.get("username").toString(), treeMap.get("password").toString(), treeMap.get("level").toString());
            managedUser.setDate(treeMap.get("date").toString());
            return new Result.Success<ManagedUser>(managedUser, "Modify user successfully!", R.string.result_success_modify_user);
        } else if (result[0] instanceof Result.Error) {
            return (Result.Error) result[0];
        } else {
            return (Result.Problem) result[0];
        }
    }

    public Result<ManagedUser> changePassword(String username, String oldPwd, String newPwd, String repeatPwd) {
        ManagedUser managedUser = null;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_PASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("oldPwd", oldPwd);
        params.put("newPwd01", newPwd);
        params.put("newPwd02", repeatPwd);
        Result<String>[] result = restfulApiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
                return new Result.Error("Fail to change password.", R.string.result_fail_change_pwd);
            }
            LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
            managedUser = new ManagedUser(treeMap.get("username").toString(), treeMap.get("password").toString(), treeMap.get("level").toString());
            managedUser.setDate(treeMap.get("date").toString());
            return new Result.Success<ManagedUser>(managedUser, "Change password successfully!", R.string.result_success_change_pwd);
        } else if (result[0] instanceof Result.Error) {
            return (Result.Error) result[0];
        } else {
            return (Result.Problem) result[0];
        }
    }
}
