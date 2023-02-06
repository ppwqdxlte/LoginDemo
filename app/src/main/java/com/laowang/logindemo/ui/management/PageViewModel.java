package com.laowang.logindemo.ui.management;

import android.util.Log;
import android.widget.TableRow;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.ManagementDataSource;
import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.ui.login.LoggedInUserView;
import com.laowang.logindemo.ui.management.formstate.ChangeFormState;
import com.laowang.logindemo.ui.management.formstate.CreateFormState;
import com.laowang.logindemo.ui.management.formstate.DeleteFormState;
import com.laowang.logindemo.ui.management.formstate.ModifyFormState;
import com.laowang.logindemo.ui.management.result.ChangeResult;
import com.laowang.logindemo.ui.management.result.CreateResult;
import com.laowang.logindemo.ui.management.result.DeleteResult;
import com.laowang.logindemo.ui.management.result.ModifyResult;

import java.util.Map;

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

    private MutableLiveData<CreateFormState> createFormState = new MutableLiveData<>();
    private MutableLiveData<CreateResult> createResult = new MutableLiveData<>();
    private MutableLiveData<ModifyFormState> modifyFormState = new MutableLiveData<>();
    private MutableLiveData<ModifyResult> modifyResult = new MutableLiveData<>();
    private MutableLiveData<DeleteFormState> deleteFormState = new MutableLiveData<>();
    private MutableLiveData<DeleteResult> deleteResult = new MutableLiveData<>();
    private MutableLiveData<ChangeFormState> changeFormState = new MutableLiveData<>();
    private MutableLiveData<ChangeResult> changeResult = new MutableLiveData<>();

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

    public void createDateChanged(ManagementViewModel fatherViewModel,String username, String pwd1, String pwd2) {
        Integer code = 0;
        if ((code = isUsernameValid(fatherViewModel,username)) != 0) {
            createFormState.setValue(new CreateFormState(code, null, null, null));
        } else if ((code = isPasswordValid(pwd1)) != 0) {
            createFormState.setValue(new CreateFormState(null,code,null,null));
        } else if ((code = isPwd1EqualsPwd2(pwd1,pwd2)) != 0){
            createFormState.setValue(new CreateFormState(null,null,code,null));
        } else {
            createFormState.setValue(new CreateFormState(true));
        }
    }

    /*
    不稳定，注释掉了
    private Integer isRadioGroupValid(int checkedRadioButtonId) {
        Log.e("checkedRadioButtonId",checkedRadioButtonId+"");
        if (checkedRadioButtonId < 0){
            return R.string.not_check_role;
        }
        return 0;
    }*/

    private Integer isPwd1EqualsPwd2(String pwd1, String pwd2) {
        if (pwd1 != null && pwd2 != null && !pwd1.trim().equals(pwd2.trim())){
            return R.string.two_pwd_not_equal;
        }
        return 0;
    }

    private Integer isPasswordValid(String pwd1) {
        if (pwd1 == null || pwd1.trim().length() < 1){
            return R.string.invalid_password;
        }
        return 0;
    }

    private Integer isUsernameValid(ManagementViewModel fatherViewModel,String username) {
        // 判断是否存在
        if (fatherViewModel != null && fatherViewModel.getTableRows().getValue().keySet().contains(username)) {
            return R.string.already_exists;
        }
        // 长度大于4位,null情况先不判断
        if (username!=null && username.trim().length() < 4){
            return R.string.short_name;
        }
        // 返回 0 表示没问题
        return 0;
    }

    public void create(ManagementViewModel fatherViewModel,String username, String pwd1, String pwd2, int checkedRadioButtonId) {
        Result<LoggedInUser> result = fatherViewModel.create(username, pwd1, checkedRadioButtonId);
        // 设置 create 结果消息提示
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            createResult.setValue(new CreateResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            // 添加失败提示
            createResult.setValue(new CreateResult(R.string.fail_create));
        }
    }

    public LiveData<CreateFormState> getCreateFormState() {
        return createFormState;
    }

    public LiveData<ModifyFormState> getModifyFormState() {
        return modifyFormState;
    }

    public LiveData<DeleteFormState> getDeleteFormState() {
        return deleteFormState;
    }

    public LiveData<ChangeFormState> getChangeFormState() {
        return changeFormState;
    }

    public LiveData<CreateResult> getCreateResult() {
        return createResult;
    }

    public LiveData<ModifyResult> getModifyResult() {
        return modifyResult;
    }

    public LiveData<DeleteResult> getDeleteResult() {
        return deleteResult;
    }

    public LiveData<ChangeResult> getChangeResult() {
        return changeResult;
    }

}