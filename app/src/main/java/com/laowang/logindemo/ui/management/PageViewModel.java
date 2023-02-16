package com.laowang.logindemo.ui.management;

import android.content.Context;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.data.model.ManagedUser;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Tab页签的页面区视图模型
 */
public class PageViewModel extends ViewModel {

    /**
     * 从1开始
     * mIndex 初始化时机：【注意】mIndex的初始化 ≠ getValue()的初始化！即运行到此时刻 index还没初始化！！
     * 所在的fragment对象onCreate()中“pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);”的时候
     */
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    /**
     * mText初始化时机：
     * 所在的fragment对象onCreate()中“pageViewModel.setIndex(index);”的时候
     * 【注意】匿名函数的Integer input指mIndex.value_setter()的入参index，而String类型的返回值会影响mText.value_getter()
     * Transformations.map()方法体现了一种约定，一种事件执行之间关联关系
     */
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);

    private MutableLiveData<UserFormState> mUserFormState = new MutableLiveData<>(new UserFormState());

    private MutableLiveData<UserMngResult> mUserMngResult = new MutableLiveData<>(new UserMngResult());

    public LiveData<UserFormState> getmUserFormState() {
        return mUserFormState;
    }

    public LiveData<UserMngResult> getmUserMngResult() {
        return mUserMngResult;
    }

    public void setmUserFormState(UserFormState userFormState) {
        this.mUserFormState.setValue(userFormState);
    }

    public void setmUserMngResult(UserMngResult userMngResult) {
        this.mUserMngResult.setValue(userMngResult);
    }

    /**
     * 方法调用地点：fragment.onCreate()。
     *
     * @param index 序号
     */
    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    /**
     * 方法调用地点：fragment.onCreateView()。
     * 【注意】上述调用地点主要添加【监视函数】，真正的执行时机除了手动显示调用，还有就是在index_setter()的时候
     * 而一旦监听到 text_getter()发生变化，则马上影响【UI显示】
     *
     * @return LiveData mText
     */
    public LiveData<String> getText() {
        return mText;
    }

    public void formDataChanged(MngViewModel mngViewModel, String newUsername, String newPwd, String repeatPwd) {
        UserFormState userFormState = new UserFormState(isUsernameMeet(mngViewModel, newUsername), isNewpwdMeet(newPwd), isRepeatpwdMeet(newPwd, repeatPwd));
        mUserFormState.setValue(userFormState);
    }

    public void formDataChanged(MngViewModel mngViewModel, String selectedName, String oldPwd, String newPwd, String repeatPwd) {
        UserFormState userFormState = new UserFormState(isSelectednameMeet(selectedName), isOldpwdMeet(mngViewModel, oldPwd, selectedName), isNewpwdMeet(newPwd), isRepeatpwdMeet(newPwd, repeatPwd));
        mUserFormState.setValue(userFormState);
    }

    public void formDataChanged(String oldPwd, String newPwd, String repeatPwd, MngViewModel mngViewModel) {
        LoginRepository repo = LoginRepository.getInstance(new LoginDataSource());
        UserFormState userFormState = new UserFormState(true, isOldpwdMeet(mngViewModel, oldPwd, repo.getUser().getDisplayName()), isNewpwdMeet(newPwd), isRepeatpwdMeet(newPwd, repeatPwd));
        mUserFormState.setValue(userFormState);
    }

    public void formDataChanged(String selectedName) {
        UserFormState userFormState = new UserFormState(isSelectednameMeet(selectedName));
        mUserFormState.setValue(userFormState);
    }

    public Integer isSelectednameMeet(String selectedName) {
        if (selectedName == null || selectedName.length() == 0) return R.string.err_select_user;
        return 1;
    }

    public Integer isUsernameMeet(MngViewModel mngViewModel, String username) {
        if (username == null) return R.string.err_username_null;
        if (username.length() <= 3 || username.length() >= 19) return R.string.err_username_length;
        // 判断是否已存在
        if (mngViewModel.getTableRows().getValue().keySet().contains(username)) {
            return R.string.err_name_exist;
        }
        return 1; // 1表示ture,其它数值都表示错误
    }

    public Integer isNewpwdMeet(String newPwd) {
        if (newPwd == null) return R.string.err_pwd_null;
        if (newPwd.length() <= 3 || newPwd.length() >= 19) return R.string.err_pwd_length;
        return 1; // 1表示ture,其它数值都表示错误
    }

    public Integer isRepeatpwdMeet(String newPwd, String repeatPwd) {
        if (!repeatPwd.equals(newPwd)) return R.string.err_pwd_not_equal;
        return 1; // 1表示ture,其它数值都表示错误
    }

    public Integer isOldpwdMeet(MngViewModel mngViewModel, String oldPwd, String selectedName) {
        if (selectedName == null || selectedName.equals("")) {
            return R.string.err_select_user;
        }
        if (mngViewModel.getmManagedUsers().getValue().get(selectedName) == null || !mngViewModel.getmManagedUsers().getValue().get(selectedName).getPassword().equals(oldPwd)) {
            return R.string.err_pwd_old;
        }
        return 1;
    }

    public void createUser(MngViewModel mngViewModel, Context context, String newUsername, String newPwd, String repeatPwd, int checkedRadioButtonId) {
        // 提交时表单验证+预设结果
        Integer resultCode = null;
        if ((resultCode = (isUsernameMeet(mngViewModel, newUsername))) != 1
                || (resultCode = (isNewpwdMeet(newPwd))) != 1
                || (resultCode = (isRepeatpwdMeet(newPwd, repeatPwd))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        Result<ManagedUser> result = mngViewModel.getDataSource().getValue().createUser(newUsername, newPwd, checkedRadioButtonId);
        // 设置 create 结果消息提示
        if (result instanceof Result.Success) {
            ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
            // mngViewModel tableRowMap 添加 user
            LoggedInUser rowUser = new LoggedInUser(UUID.randomUUID().toString(),
                    data.getUsername(), data.getPassword(), data.getRoleCode(), data.getDate());
            TableRow row = mngViewModel.addUserInfoInRow(context, rowUser, mngViewModel.getTableRows().getValue().size() + 1);
            mngViewModel.getTableRows().getValue().put(data.getUsername(), row);
            // 先清空
            row.setOnClickListener(null);
            row.setClickable(true);
            // 再创建
            row.setOnClickListener(v -> {
                // 我直接在这里改状态行不？不调用控件了行不？让控件跟随 状态变化行不？？？Multable<String> selectedName...
                String selectedName = ((TextView) row.getChildAt(1)).getText().toString();
                mngViewModel.setmSelectedName(selectedName);
            });
            UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_create_user);
            mUserMngResult.setValue(userMngResult);
        } else {
            // 添加失败提示
            UserMngResult userMngResult = new UserMngResult(R.string.result_fail_create_user);
            mUserMngResult.setValue(userMngResult);
        }
    }

    public void modifyUser(MngViewModel mngViewModel, String selectedName, String oldPwd, String newPwd, String repeatPwd) {
        // 提交时表单验证+预设结果
        Integer resultCode = null;
        if ((resultCode = (isSelectednameMeet(selectedName))) != 1
                || (resultCode = (isOldpwdMeet(mngViewModel, oldPwd, selectedName))) != 1
                || (resultCode = (isNewpwdMeet(newPwd))) != 1
                || (resultCode = (isRepeatpwdMeet(newPwd, repeatPwd))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        Result<ManagedUser> result = mngViewModel.getDataSource().getValue().modifyUser(selectedName, oldPwd, newPwd, repeatPwd);
        // 设置 modify 结果消息提示
        if (result instanceof Result.Success) {
            ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
            UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_modify_user);
            mUserMngResult.setValue(userMngResult);
        } else {
            // 修改失败提示
            UserMngResult userMngResult = new UserMngResult(R.string.result_fail_modify_user);
            mUserMngResult.setValue(userMngResult);
        }
    }

    public void changePassword(MngViewModel mngViewModel, String newPwd, String repeatPwd) {
        LoginRepository instance = LoginRepository.getInstance(new LoginDataSource());
        String username = instance.getUser().getDisplayName();
        String oldPwd = instance.getUser().getPassword();
        // 提交时表单验证+预设结果
        Integer resultCode = null;
        if ((resultCode = (isOldpwdMeet(mngViewModel, oldPwd, username))) != 1
                || (resultCode = (isNewpwdMeet(newPwd))) != 1
                || (resultCode = (isRepeatpwdMeet(newPwd, repeatPwd))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        Result<ManagedUser> result = mngViewModel.getDataSource().getValue().changePassword(username, oldPwd, newPwd, repeatPwd);
        // 设置 change 结果消息提示
        if (result instanceof Result.Success) {
            ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
            UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_change_pwd);
            mUserMngResult.setValue(userMngResult);
        } else {
            // 改密失败提示
            UserMngResult userMngResult = new UserMngResult(R.string.result_fail_change_pwd);
            mUserMngResult.setValue(userMngResult);
        }
    }

    public void deleteUser(MngViewModel mngViewModel, String selectedName) {
        // 提交时表单验证+预设结果
        Integer resultCode = null;
        if ((resultCode = (isSelectednameMeet(selectedName))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        Result<ManagedUser> result = mngViewModel.getDataSource().getValue().deleteUser(selectedName);
        // 设置 delete 结果消息提示
        if (result instanceof Result.Success) {
            ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
            // mngViewModel tableRowMap 删除 user
            Map<String, TableRow> map = mngViewModel.getTableRows().getValue();
            map.remove(data.getUsername());
            int sn = 1;
            for (String key : map.keySet()) {
                TableRow tableRow = map.get(key);
                TextView SN = (TextView) (tableRow.getChildAt(0));
                SN.setText(sn+++"");
            }
            UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_delete_user);
            mUserMngResult.setValue(userMngResult);
        } else {
            // 删除失败提示
            UserMngResult userMngResult = new UserMngResult(R.string.result_fail_delete_user);
            mUserMngResult.setValue(userMngResult);
        }
    }
}