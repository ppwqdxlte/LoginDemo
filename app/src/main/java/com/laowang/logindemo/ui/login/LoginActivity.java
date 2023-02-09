package com.laowang.logindemo.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.laowang.logindemo.Main0Activity;
import com.laowang.logindemo.MainActivity;
import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.databinding.ActivityLoginBinding;
import com.laowang.logindemo.databinding.ActivityMainBinding;
import com.laowang.logindemo.ui.login.LoginViewModel;
import com.laowang.logindemo.ui.login.LoginViewModelFactory;

public class LoginActivity extends AppCompatActivity {
    /*
     * jetpack护法ViewModel
    * */
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Activity在创建时的必要代码 */
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        /* XXXViewModel（/ui/XXX/..）对象，不是谁（Activity）都有的，这句是初始化viewModel对象的基本语法 */
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        /* 获得页面构件 */
        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;
        /* 可变LiveData观察页面表单状态变化，输入不规范会有提醒，满足条件就激活登录按钮 */
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });
        /* 可变LiveData观察登录结果的变化，不起眼的方法，在这里登录跳转 */
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                    // Complete and destroy login activity once successful，登录成功就销毁登陆页面
                    finish(); // this.finish()调用基类的隐藏方法，从安卓手机后台还能调出来，假的销毁
                    // 打开主页 【注意】只有在AndroidManifest.xml中声明过的Activity才能被拿来使用
                    /* 根据当前用户权限显示不同的导航项，试过代码修改，由于没找到简单方法，故而最笨的方法，一个权限对应一套页面。。。 */
                    if (LoginRepository.getInstance(new LoginDataSource()).getUser().getLevel().contains("1")){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(LoginActivity.this, Main0Activity.class);
                        startActivity(intent);
                    }
                }
                setResult(Activity.RESULT_OK);
            }
        });
        /* 文本观察员，文字发生改变的监听器 */
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 影响 loginFormState 的变化
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        /* 页面部件绑定 文本观察员 */
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        /* 密码框 绑定 编辑器动作监听，主要为了处理输入法界面点击了enter后的动作 */
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //  IME(Input Method Editor): 输入法编辑器。通常简作输入法
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // 从界面中获取username,password传参，WHAT？为啥在这里有登录？
//                    Log.d("输入法界面点了enter回车","除了手动点击按钮空间，点击输入法的回车一样会登录");
                    loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                // 返回false表示点击后,隐藏软键盘。返回true表示保留软键盘
                return false;
            }
        });
        /* 按钮部件 添加单击监听器 */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示进度条
                loadingProgressBar.setVisibility(View.VISIBLE);
                // 登录，从界面中获取username,password传参
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    }
    /** 登陆成功提示
     * @param model 携带了username
     */
    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        Toast.makeText(getApplicationContext(), welcome,Toast.LENGTH_LONG).show();
    }
    /** 登陆失败提示
     * @param errorString R.string.XXX
     */
    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}