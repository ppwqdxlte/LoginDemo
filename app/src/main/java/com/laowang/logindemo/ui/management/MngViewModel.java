package com.laowang.logindemo.ui.management;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.MngDataSource;
import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.model.LoggedInUser;
import com.laowang.logindemo.data.model.ManagedUser;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class MngViewModel extends ViewModel {

    private final LiveData<LoginRepository> loginRepository;

    private final LiveData<MngDataSource> dataSource;

    private final MutableLiveData<String> mText;

    private MutableLiveData<Map<String, TableRow>> mTableRows;

    private final MutableLiveData<Map<String, ManagedUser>> mManagedUsers;

    private final MutableLiveData<String> mSelectedName;

    public static Fragment fragment;

    private Context context;
    private PageViewModel pageViewModel;

    public MngViewModel() {
        loginRepository = new MutableLiveData<>(LoginRepository.getInstance(new LoginDataSource()));
        dataSource = new MutableLiveData<>(new MngDataSource(fragment));
        mText = new MutableLiveData<>();
        mText.setValue("USER LIST");
        mTableRows = new MutableLiveData<>();
        mTableRows.setValue(new HashMap<>());
        mManagedUsers = new MutableLiveData<>();
        mManagedUsers.setValue(new HashMap<>());
        mSelectedName = new MutableLiveData<>();
        dataSource.getValue().getMyViewModel().getmCreateResult().observe(fragment.getViewLifecycleOwner(), result -> {
            if (result == null) return;
            if (result instanceof Result.Success) {
                ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
                LoggedInUser rowUser = new LoggedInUser(UUID.randomUUID().toString(),
                        data.getUsername(), data.getPassword(), data.getRoleCode(), data.getDate());
                TableRow row = MngViewModel.this.addUserInfoInRow(MngViewModel.this.context, rowUser, MngViewModel.this.getTableRows().getValue().size() + 1);
                MngViewModel.this.getTableRows().getValue().put(data.getUsername(), row);
                MngViewModel.this.getmManagedUsers().getValue().put(data.getUsername(),data);
                row.setOnClickListener(null);
                row.setClickable(true);
                row.setOnClickListener(v -> {
                    String selectedName = ((TextView) row.getChildAt(1)).getText().toString();
                    MngViewModel.this.setmSelectedName(selectedName);
                });
                UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_create_user);
                pageViewModel.setmUserMngResult(userMngResult);
            } else {
                UserMngResult userMngResult = new UserMngResult(R.string.result_fail_create_user);
                pageViewModel.setmUserMngResult(userMngResult);
            }
        });
        dataSource.getValue().getMyViewModel().getmModifyResult().observe(fragment.getViewLifecycleOwner(), result -> {
            if (result == null) return;
            if (result instanceof Result.Success) {
                ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
                UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_modify_user);
                pageViewModel.setmUserMngResult(userMngResult);
            } else {
                UserMngResult userMngResult = new UserMngResult(R.string.result_fail_modify_user);
                pageViewModel.setmUserMngResult(userMngResult);
            }
        });
        dataSource.getValue().getMyViewModel().getmDeleteResult().observe(fragment.getViewLifecycleOwner(), result -> {
            if (result == null) return;
            if (result instanceof Result.Success) {
                ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
                Map<String, TableRow> map = MngViewModel.this.getTableRows().getValue();
                map.remove(data.getUsername());
                int sn = 1;
                for (String key : map.keySet()) {
                    TableRow tableRow = map.get(key);
                    TextView SN = (TextView) (tableRow.getChildAt(0));
                    SN.setText(sn+++"");
                }
                UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_delete_user);
                pageViewModel.setmUserMngResult(userMngResult);
            } else {
                UserMngResult userMngResult = new UserMngResult(R.string.result_fail_delete_user);
                pageViewModel.setmUserMngResult(userMngResult);
            }
        });
        dataSource.getValue().getMyViewModel().getmChangeResult().observe(fragment.getViewLifecycleOwner(), result -> {
            if (result == null) return;
            if (result instanceof Result.Success) {
                ManagedUser data = ((Result.Success<ManagedUser>) result).getData();
                UserMngResult userMngResult = new UserMngResult(data, R.string.result_success_change_pwd);
                pageViewModel.setmUserMngResult(userMngResult);
            } else {
                UserMngResult userMngResult = new UserMngResult(R.string.result_fail_change_pwd);
                pageViewModel.setmUserMngResult(userMngResult);
            }
        });
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Map<String, TableRow>> getTableRows() {
        return mTableRows;
    }

    public void setmTableRows(Map<String, TableRow> tableRows) {
        this.mTableRows.setValue(tableRows);
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public PageViewModel getPageViewModel() {
        return pageViewModel;
    }

    public void setPageViewModel(PageViewModel pageViewModel) {
        this.pageViewModel = pageViewModel;
    }

    public void updateUserList(Context context) {
        LoggedInUser loggedInUser = loginRepository.getValue().getUser();
        TreeMap<TableRow, String> reverseMap = new TreeMap<>((o1, o2) -> {
            TextView SN1 = (TextView) (o1.getChildAt(0));
            TextView SN2 = (TextView) (o2.getChildAt(0));
            int i = Integer.parseInt(SN1.getText().toString()) - Integer.parseInt(SN2.getText().toString());
            if (i < 0) {
                return 1;
            } else if (i > 0) {
                return -1;
            } else {
                return 0;
            }
        });
        if (loggedInUser.getLevel().contains("1")) {
            mTableRows.getValue().clear();
            mManagedUsers.getValue().clear();
            List<LoggedInUser> users = dataSource.getValue().queryUserList();
            for (int i = 0; i < users.size(); i++) {
                LoggedInUser user = users.get(i);
                TableRow tableRow = addUserInfoInRow(context, user, i + 1);
                reverseMap.put(tableRow, user.getDisplayName());
                ManagedUser mngUser = new ManagedUser(user.getDisplayName(), user.getPassword());
                mManagedUsers.getValue().put(mngUser.getUsername(), mngUser);
            }
        } else {
            if (!mTableRows.getValue().containsKey(loggedInUser.getDisplayName())) { // 尚不包含
                TableRow tableRow = addUserInfoInRow(context, loggedInUser, 1);
                reverseMap.put(tableRow, loggedInUser.getDisplayName());
                ManagedUser mngUser = new ManagedUser(loggedInUser.getDisplayName(), loggedInUser.getPassword());
                mManagedUsers.getValue().put(mngUser.getUsername(), mngUser);
            }
        }
        Map<String, TableRow> map = new HashMap<>();
        for (TableRow tableRow : reverseMap.keySet()) {
            map.put(reverseMap.get(tableRow), tableRow);
        }
        mTableRows.setValue(map);
    }

    public TableRow addUserInfoInRow(Context context, LoggedInUser user, int serialnumber) {
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