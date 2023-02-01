package com.laowang.logindemo.ui.management;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.ManagementDataSource;
import com.laowang.logindemo.data.model.LoggedInUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视图模型应该处理数据有关，management页面肯定不止文字啦！还有 grid列表，底部tab页签，每个tab页还要包含 输入框 和 按钮！！
 */
public class ManagementViewModel extends ViewModel {
    //    private static AtomicInteger count = new AtomicInteger(0);
    /* 调用LoginRepository保存当前用户的状态 */
    private final LiveData<LoginRepository> loginRepository;
    /* 数据源 */
    private final LiveData<ManagementDataSource> dataSource;
    // 旧代码
    private final MutableLiveData<String> mText;
    /**
     * 用户列表
     */
    private final MutableLiveData<Map<String, TableRow>> mTableRows;

    public ManagementViewModel() {
        /* Log.e("管理视图模型","创建了"+count.incrementAndGet()+"次。"); 始终 只有 1 次，说明页面生命周期中视图模型对象只会创建一次，
        如果只在构造方法获得数据，那么app运行期间都不会得到刷新！所以比如列表的获取，应该在 activity中查询，每次进入页面都会得到刷新！！！ */
        loginRepository = new MutableLiveData<>(LoginRepository.getInstance(new LoginDataSource()));
        dataSource = new MutableLiveData<>(new ManagementDataSource());
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
        mTableRows = new MutableLiveData<>();
        mTableRows.setValue(new HashMap<>()); // 避免空指针异常
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Map<String, TableRow>> getTableRows() {
        return mTableRows;
    }

    /**
     * 用户权限1就查询所有用户，权限level==0则只放自己的信息
     */
    public void updateUserList(Context context) {
        LoggedInUser loggedInUser = loginRepository.getValue().getUser();
        if (loggedInUser.getLevel().contains("1")) {
            //先清空
            mTableRows.getValue().clear();
            List<LoggedInUser> users = dataSource.getValue().queryUserList();
            for (int i = 0; i < users.size(); i++) {
                LoggedInUser user = users.get(i);
                TableRow tableRow = addUserInfoInRow(context, user, i + 1);
                mTableRows.getValue().put(user.getDisplayName(),tableRow);
            }
        } else {
            if (!mTableRows.getValue().containsKey(loggedInUser.getDisplayName())) { // 尚不包含
                TableRow tableRow = addUserInfoInRow(context, loggedInUser, 1);
                mTableRows.getValue().put(loggedInUser.getDisplayName(), tableRow);
            }
        }
    }

    private TableRow addUserInfoInRow(Context context, LoggedInUser user, int serialnumber) {
        TableRow userRow = new TableRow(context);
        TextView sn = new TextView(context);
        sn.setText(serialnumber+"");
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
}