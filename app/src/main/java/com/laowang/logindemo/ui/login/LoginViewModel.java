package com.laowang.logindemo.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.R;

/**
 * ViewModel 包含:
 *  LoginRepository? /data/model/.. 构造时候初始化，设置loginRepository，没有getter
 *  LoginFormState?  /ui/login/..
 *  LoginResult?     /ui/login/..
 */
public class LoginViewModel extends ViewModel {
    /** 可变LiveData: 表单状态(是否合法) */
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    /** 可变LiveData: 结果 */
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    /** 仓库：维护登录状态和用户凭据信息的内存缓存 */
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }
    /**
     * 登录，很显然【包装模式】方法层层调用层层加码，这层的登录方法作用主要 显示登录结果
     * @param username username
     * @param password password
     */
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);
        // 设置登录结果消息提示
        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            // 用户不存在提示
            loginResult.setValue(new LoginResult(R.string.user_not_exists));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 1;
    }
}