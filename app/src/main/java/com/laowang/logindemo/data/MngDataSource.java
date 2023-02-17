package com.laowang.logindemo.data;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.R;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.data.model.ManagedUser;
import com.laowang.logindemo.ui.MyViewModel;
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

    public MyViewModel getMyViewModel() {
        return myViewModel;
    }

    private MyViewModel myViewModel;
    /**
     * 1-create user
     * 2-modify user
     * 3-delete user
     * 4-change password
     */
    private Integer operationCode;

    public MngDataSource(Fragment fragment) {
        myViewModel = new ViewModelProvider(fragment).get(MyViewModel.class);
        myViewModel.getmResultString().observe(fragment.getViewLifecycleOwner(), stringResult -> {
            if (stringResult == null) return;
            // 设置 Result<ManagedUser>
            if (stringResult instanceof Result.Success) {
                Result.Success<String> success = (Result.Success<String>) stringResult;
                String jsonStr = success.getData();
                com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
                if (operationCode == 1) { // create user
                    if (fromJson.getData() != null) {
                        LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
                        ManagedUser managedUser = new ManagedUser(
                                treeMap.get("username").toString(),
                                treeMap.get("password").toString(),
                                treeMap.get("level").toString());
                        managedUser.setDate(treeMap.get("date").toString());
                        myViewModel.setmCreateResult(new Result.Success<ManagedUser>(managedUser, "Create user successfully!", R.string.result_success_create_user));
                    } else {
                        myViewModel.setmCreateResult(new Result.Error("Fail to create user.", R.string.result_fail_create_user));
                    }
                } else if (operationCode == 2) { // modify user
                    if (fromJson.getData() != null) {
                        LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
                        ManagedUser managedUser = new ManagedUser(
                                treeMap.get("username").toString(),
                                treeMap.get("password").toString(),
                                treeMap.get("level").toString());
                        managedUser.setDate(treeMap.get("date").toString());
                        myViewModel.setmModifyResult(new Result.Success<ManagedUser>(managedUser, "Modify user successfully!", R.string.result_success_modify_user));
                    } else {
                        myViewModel.setmModifyResult(new Result.Error("Fail to modify user.", R.string.result_fail_modify_user));
                    }
                } else if (operationCode == 3) { // delete user
                    if (fromJson.getData() != null) {
                        LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
                        ManagedUser managedUser = new ManagedUser(treeMap.get("username").toString(), null, null);
                        myViewModel.setmDeleteResult(new Result.Success<ManagedUser>(managedUser, "Delete user successfully!", R.string.result_success_delete_user));
                    } else {
                        myViewModel.setmDeleteResult(new Result.Error("Fail to delete user.", R.string.result_fail_delete_user));
                    }
                } else {                         // change password
                    if (fromJson.getData() != null) {
                        LinkedTreeMap treeMap = (LinkedTreeMap) (fromJson.getData());
                        ManagedUser managedUser = new ManagedUser(
                                treeMap.get("username").toString(),
                                treeMap.get("password").toString(),
                                treeMap.get("level").toString());
                        managedUser.setDate(treeMap.get("date").toString());
                        myViewModel.setmChangeResult(new Result.Success<ManagedUser>(managedUser, "Change password successfully!", R.string.result_success_change_pwd));
                    } else {
                        myViewModel.setmChangeResult(new Result.Error("Fail to change password.", R.string.result_fail_change_pwd));
                    }
                }
            } else if (stringResult instanceof Result.Error) {
                if (operationCode == 1) {
                    myViewModel.setmCreateResult((Result.Error) stringResult);
                } else if (operationCode == 2) {
                    myViewModel.setmModifyResult((Result.Error) stringResult);
                } else if (operationCode == 3) {
                    myViewModel.setmDeleteResult((Result.Error) stringResult);
                } else {
                    myViewModel.setmChangeResult((Result.Error) stringResult);
                }
            } else {
                if (operationCode == 1) {
                    myViewModel.setmCreateResult((Result.Problem) stringResult);
                } else if (operationCode == 2) {
                    myViewModel.setmModifyResult((Result.Problem) stringResult);
                } else if (operationCode == 3) {
                    myViewModel.setmDeleteResult((Result.Problem) stringResult);
                } else {
                    myViewModel.setmChangeResult((Result.Problem) stringResult);
                }
            }
        });
    }

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
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
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

    public void createUser(String newUsername, String newPwd, int checkedRadioButtonId) {
        operationCode = 1;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_CREATE_USER;
        Map<String, String> params = new HashMap<>();
        params.put("username", newUsername);
        params.put("password", newPwd);
        params.put("permisstionIndex", checkedRadioButtonId + "");
        restfulApiHandler.postAsync(myViewModel, strUrl, params);
    }

    public void modifyUser(String selectedName, String oldPwd, String newPwd, String repeatPwd) {
        operationCode = 2;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_PASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("username", selectedName);
        params.put("oldPwd", oldPwd);
        params.put("newPwd01", newPwd);
        params.put("newPwd02", repeatPwd);
        restfulApiHandler.postAsync(myViewModel, strUrl, params);
    }

    public void deleteUser(String selectedName) {
        operationCode = 3;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_DELETE_USER;
        Map<String, String> params = new HashMap<>();
        params.put("selectedUser", selectedName);
        restfulApiHandler.postAsync(myViewModel, strUrl, params);
    }

    public void changePassword(String username, String oldPwd, String newPwd, String repeatPwd) {
        operationCode = 4;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_PASSWORD;
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("oldPwd", oldPwd);
        params.put("newPwd01", newPwd);
        params.put("newPwd02", repeatPwd);
        restfulApiHandler.postAsync(myViewModel, strUrl, params);
    }
}
