package com.laowang.logindemo.ui.management;

import android.util.Log;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 视图模型应该处理数据有关，management页面肯定不止文字啦！还有 grid列表，底部tab页签，每个tab页还要包含 输入框 和 按钮！！
 */
public class ManagementViewModel extends ViewModel {
//    private static AtomicInteger count = new AtomicInteger(0);
    private final MutableLiveData<String> mText;
    /**
     * 用户列表
     */
    private final MutableLiveData<List<TableRow>> mTableRows;

    public ManagementViewModel() {
        /* Log.e("管理视图模型","创建了"+count.incrementAndGet()+"次。"); 始终 只有 1 次，说明页面生命周期中视图模型对象只会创建一次，
        如果只在构造方法获得数据，那么app运行期间都不会得到刷新！所以比如列表的获取，应该在 activity中查询，每次进入页面都会得到刷新！！！ */
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
        mTableRows = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<TableRow>> getTableRows() {
        return mTableRows;
    }

}