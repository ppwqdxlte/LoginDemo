package com.laowang.logindemo.ui.management;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.MngDataSource;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.data.model.ManagedUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MngViewModel extends ViewModel {

    private final LiveData<LoginRepository> loginRepository;

    private final LiveData<MngDataSource> dataSource;

    private final MutableLiveData<String> mText;

    private final MutableLiveData<Map<String, TableRow>> mTableRows;

    private final MutableLiveData<Map<String, ManagedUser>> mManagedUsers;

    private final MutableLiveData<String> mSelectedName;

    public MngViewModel() {
        loginRepository = new MutableLiveData<>(LoginRepository.getInstance(new LoginDataSource()));
        dataSource = new MutableLiveData<>(new MngDataSource());
        mText = new MutableLiveData<>();
        mText.setValue("USER LIST");
        mTableRows = new MutableLiveData<>();
        mTableRows.setValue(new HashMap<>()); // 避免空指针异常
        mManagedUsers = new MutableLiveData<>();
        mManagedUsers.setValue(new HashMap<>());
        mSelectedName = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Map<String, TableRow>> getTableRows() {
        return mTableRows;
    }

    public LiveData<Map<String, ManagedUser>> getmManagedUsers() {
        return mManagedUsers;
    }

    public LiveData<MngDataSource> getDataSource() {
        return dataSource;
    }

    public LiveData<String> getmSelectedName() {
        return mSelectedName;
    }

    public void setmSelectedName(String value) {
        this.mSelectedName.setValue(value);
    }

    public void updateUserList(Context context) {
        LoggedInUser loggedInUser = loginRepository.getValue().getUser();
        if (loggedInUser.getLevel().contains("1")) {
            mTableRows.getValue().clear();
            mManagedUsers.getValue().clear();
            List<LoggedInUser> users = dataSource.getValue().queryUserList();
            for (int i = 0; i < users.size(); i++) {
                LoggedInUser user = users.get(i);
                TableRow tableRow = addUserInfoInRow(context, user, i + 1);
                mTableRows.getValue().put(user.getDisplayName(), tableRow);
                ManagedUser mngUser = new ManagedUser(user.getDisplayName(), user.getPassword());
                mManagedUsers.getValue().put(mngUser.getUsername(), mngUser);
            }
        } else {
            if (!mTableRows.getValue().containsKey(loggedInUser.getDisplayName())) { // 尚不包含
                TableRow tableRow = addUserInfoInRow(context, loggedInUser, 1);
                mTableRows.getValue().put(loggedInUser.getDisplayName(), tableRow);
                ManagedUser mngUser = new ManagedUser(loggedInUser.getDisplayName(), loggedInUser.getPassword());
                mManagedUsers.getValue().put(mngUser.getUsername(), mngUser);
            }
        }
    }

    private TableRow addUserInfoInRow(Context context, LoggedInUser user, int serialnumber) {
        TableRow userRow = new TableRow(context);
        TextView sn = new TextView(context);
        sn.setText(serialnumber + "");
        TextView username = new TextView(context);
        username.setText(user.getDisplayName());
        TextView role = new TextView(context);
        role.setText(user.getLevel().contains("0") ? "regular" : "administrator");
        TextView createdTime = new TextView(context);
        createdTime.setText(user.getDate());
        userRow.addView(sn);
        userRow.addView(username);
        userRow.addView(role);
        userRow.addView(createdTime);
        return userRow;
    }

    public TableRow addTableHeadInRow(Context context) {
        TableRow userRow = new TableRow(context);
        TextView sn = new TextView(context);
        sn.setText("SN");
        TextView username = new TextView(context);
        username.setText("Username");
        TextView role = new TextView(context);
        role.setText("Role");
        TextView createdTime = new TextView(context);
        createdTime.setText("CreatedTime");
        userRow.addView(sn);
        userRow.addView(username);
        userRow.addView(role);
        userRow.addView(createdTime);
        return userRow;
    }
}