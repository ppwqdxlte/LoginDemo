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

public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();

    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);

    private MutableLiveData<UserFormState> mUserFormState = new MutableLiveData<>(new UserFormState());

    private MutableLiveData<UserMngResult> mUserMngResult = new MutableLiveData<>(new UserMngResult());

    public LiveData<UserFormState> getmUserFormState() {
        return mUserFormState;
    }

    public LiveData<UserMngResult> getmUserMngResult() {
        return mUserMngResult;
    }

    private Integer operationCode;

    public void setmUserFormState(UserFormState userFormState) {
        this.mUserFormState.setValue(userFormState);
    }

    public void setmUserMngResult(UserMngResult userMngResult) {
        this.mUserMngResult.setValue(userMngResult);
    }

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

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
        if (mngViewModel.getTableRows().getValue().keySet().contains(username)) {
            return R.string.err_name_exist;
        }
        return 1;
    }

    public Integer isNewpwdMeet(String newPwd) {
        if (newPwd == null) return R.string.err_pwd_null;
        if (newPwd.length() <= 3 || newPwd.length() >= 19) return R.string.err_pwd_length;
        return 1;
    }

    public Integer isRepeatpwdMeet(String newPwd, String repeatPwd) {
        if (!repeatPwd.equals(newPwd)) return R.string.err_pwd_not_equal;
        return 1;
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
        operationCode = 1;
        Integer resultCode = null;
        if ((resultCode = (isUsernameMeet(mngViewModel, newUsername))) != 1
                || (resultCode = (isNewpwdMeet(newPwd))) != 1
                || (resultCode = (isRepeatpwdMeet(newPwd, repeatPwd))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        mngViewModel.setContext(context);
        mngViewModel.setPageViewModel(this);
        mngViewModel.getDataSource().getValue().createUser(newUsername, newPwd, checkedRadioButtonId);
    }

    public void modifyUser(MngViewModel mngViewModel,Context context, String selectedName, String oldPwd, String newPwd, String repeatPwd) {
        operationCode = 2;
        Integer resultCode = null;
        if ((resultCode = (isSelectednameMeet(selectedName))) != 1
                || (resultCode = (isOldpwdMeet(mngViewModel, oldPwd, selectedName))) != 1
                || (resultCode = (isNewpwdMeet(newPwd))) != 1
                || (resultCode = (isRepeatpwdMeet(newPwd, repeatPwd))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        mngViewModel.setContext(context);
        mngViewModel.setPageViewModel(this);
        mngViewModel.getDataSource().getValue().modifyUser(selectedName, oldPwd, newPwd, repeatPwd);
    }

    public void deleteUser(MngViewModel mngViewModel,Context context, String selectedName) {
        operationCode = 3;
        Integer resultCode = null;
        if ((resultCode = (isSelectednameMeet(selectedName))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        mngViewModel.setContext(context);
        mngViewModel.setPageViewModel(this);
        mngViewModel.getDataSource().getValue().deleteUser(selectedName);
    }

    public void changePassword(MngViewModel mngViewModel, Context context,String newPwd, String repeatPwd) {
        operationCode = 4;
        LoginRepository instance = LoginRepository.getInstance(new LoginDataSource());
        String username = instance.getUser().getDisplayName();
        String oldPwd = instance.getUser().getPassword();
        Integer resultCode = null;
        if ((resultCode = (isOldpwdMeet(mngViewModel, oldPwd, username))) != 1
                || (resultCode = (isNewpwdMeet(newPwd))) != 1
                || (resultCode = (isRepeatpwdMeet(newPwd, repeatPwd))) != 1) {
            UserMngResult userMngResult = new UserMngResult(resultCode);
            mUserMngResult.setValue(userMngResult);
            return;
        }
        mngViewModel.setContext(context);
        mngViewModel.setPageViewModel(this);
        mngViewModel.getDataSource().getValue().changePassword(username, oldPwd, newPwd, repeatPwd);
    }
}