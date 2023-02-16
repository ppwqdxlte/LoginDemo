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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 视图模型应该处理数据有关，management页面肯定不止文字啦！还有 grid列表，底部tab页签，每个tab页还要包含 输入框 和 按钮！！
 * 【NOTE】ViewModel 将数据保留在内存中，这意味着开销要低于从磁盘或网络检索数据。
 * ViewModel 与一个 Activity（或其他某个生命周期所有者）相关联，在配置更改期间保留在内存中，
 * 系统会自动将 ViewModel 与发生配置更改后产生的新 Activity 实例相关联。
 * <p>
 * 【数据倒灌】现象 eg.
 * https://blog.csdn.net/weixin_39798626/article/details/112291113
 * https://blog.csdn.net/Jason_Lee155/article/details/119966408?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-0-119966408-blog-112291113.pc_relevant_3mothn_strategy_recovery&spm=1001.2101.3001.4242.1&utm_relevant_index=3
 */
public class MngViewModel extends ViewModel {
    //    private static AtomicInteger count = new AtomicInteger(0);
    /* 调用LoginRepository保存当前用户的状态 */
    private final LiveData<LoginRepository> loginRepository;
    /* 数据源 */
    private final LiveData<MngDataSource> dataSource;
    // 旧代码
    private final MutableLiveData<String> mText;
    /**
     * 用户列表
     */
    private MutableLiveData<Map<String, TableRow>> mTableRows;
    /**
     * 为了修改密码所添加
     */
    private final MutableLiveData<Map<String, ManagedUser>> mManagedUsers;
    /**
     * 为了双向绑定 选定用户名，为了修改用户和删除用户使用
     */
    private final MutableLiveData<String> mSelectedName;

    public MngViewModel() {
        /* Log.e("管理视图模型","创建了"+count.incrementAndGet()+"次。"); 始终 只有 1 次，说明页面生命周期中视图模型对象只会创建一次，
        如果只在构造方法获得数据，那么app运行期间都不会得到刷新！所以比如列表的获取，应该在 activity中查询，每次进入页面都会得到刷新！！！ */
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

    /**
     * 用户权限1就查询所有用户，权限level==0则只放自己的信息
     */
    public void updateUserList(Context context) {
        LoggedInUser loggedInUser = loginRepository.getValue().getUser();
        TreeMap<TableRow,String> reverseMap = new TreeMap<>((o1, o2) -> {
            TextView SN1 = (TextView) (o1.getChildAt(0));
            TextView SN2 = (TextView) (o2.getChildAt(0));
            int i = Integer.parseInt(SN1.getText().toString()) - Integer.parseInt(SN2.getText().toString());
            if (i<0){
                return 1;
            } else if (i > 0){
                return -1;
            } else {
                return 0;
            }
        });
        if (loggedInUser.getLevel().contains("1")) {
            //先清空
            mTableRows.getValue().clear();
            mManagedUsers.getValue().clear();
            List<LoggedInUser> users = dataSource.getValue().queryUserList();
            for (int i = 0; i < users.size(); i++) {
                LoggedInUser user = users.get(i);
                TableRow tableRow = addUserInfoInRow(context, user, i + 1);
                reverseMap.put(tableRow,user.getDisplayName());
                ManagedUser mngUser = new ManagedUser(user.getDisplayName(), user.getPassword());
                mManagedUsers.getValue().put(mngUser.getUsername(), mngUser);
            }
        } else {
            if (!mTableRows.getValue().containsKey(loggedInUser.getDisplayName())) { // 尚不包含
                TableRow tableRow = addUserInfoInRow(context, loggedInUser, 1);
                reverseMap.put(tableRow,loggedInUser.getDisplayName());
                ManagedUser mngUser = new ManagedUser(loggedInUser.getDisplayName(), loggedInUser.getPassword());
                mManagedUsers.getValue().put(mngUser.getUsername(), mngUser);
            }
        }
        Map<String,TableRow> map = new HashMap<>();
        for (TableRow tableRow : reverseMap.keySet()) {
            map.put(reverseMap.get(tableRow),tableRow);
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