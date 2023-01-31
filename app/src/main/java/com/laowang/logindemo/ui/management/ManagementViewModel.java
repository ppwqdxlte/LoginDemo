package com.laowang.logindemo.ui.management;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 视图模型应该处理数据有关，management页面肯定不止文字啦！还有 grid列表，底部tab页签，每个tab页还要包含 输入框 和 按钮！！
 */
public class ManagementViewModel extends ViewModel {

    /**
     * 可变数据：文本 TODO 还有可变用户列表，可变 tabs啊，可变 页面啊啥的。。。？？
     */
    private final MutableLiveData<String> mText;

    public ManagementViewModel() {
        // TODO 在这里设置 页面的数据[ grid, tabs, editTexts, buttons]
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    /**
     * @return 获取文字
     */
    public LiveData<String> getText() {
        return mText;
    }
    /*
    TODO：获取grid? tabs??
     */
}