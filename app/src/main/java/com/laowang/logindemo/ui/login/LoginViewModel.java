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
    /*登录状态*/
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    /*登录结果*/
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    /*LoginRepository成员变量*/
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
     *
     * @param username username
     * @param password password
     */
    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
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