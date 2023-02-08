package com.laowang.logindemo.ui.management;

import android.util.Log;
import android.widget.TableRow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.R;

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

    public void formDataChanged(MngViewModel mngViewModel, String newUsername, String newPwd, String repeatPwd, String oldPwd, String selectedName) {
//        Log.e("表单数据改变------------------》》》 ",newUsername+"\t="+newPwd+"\t="+repeatPwd+"\t="+oldPwd+"\t="+selectedName);
        mUserFormState.getValue().setNewNameError(isUsernameMeet(mngViewModel, newUsername));
        mUserFormState.getValue().setNewPwdError(isNewpwdMeet(newPwd));
        mUserFormState.getValue().setRepeatPwdError(isRepeatpwdMeet(newPwd, repeatPwd));
        mUserFormState.getValue().setOldPwdError(isOldpwdMeet(mngViewModel, oldPwd, selectedName));
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
        if (selectedName == null || selectedName.equals("")){
            return R.string.err_select_user;
        }
        if (!mngViewModel.getmManagedUsers().getValue().get(selectedName).getPassword().equals(oldPwd)) {
            return R.string.err_pwd_old;
        }
        return 1;
    }

}