package com.laowang.logindemo.ui.management;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

/**
 * Tab页签的页面区视图模型
 */
public class PageViewModel extends ViewModel {
    /*
     * 首次进管理页，设置tab1增加1轮，设置tab2再增加1轮，都变成了2,
     * 但是以后在进入此管理页，就不再自动增加了，setIndex()和getText()在PlaceholderFragment中只调用2次
     *  */
//    private static AtomicInteger mTextApplyedTimes = new AtomicInteger(0);
//    private static AtomicInteger mTextGetterTimes = new AtomicInteger(0);
//    private static AtomicInteger mIndexSetterTimes = new AtomicInteger(0);

    /**
     * 从1开始
     * mIndex 初始化时机：【注意】mIndex的初始化 ≠ getValue()的初始化！即运行到此时刻 index还没初始化！！
     * 所在的fragment对象onCreate()中“pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);”的时候
     */
    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    /**
     * mText初始化时机：
     * 所在的fragment对象onCreate()中“pageViewModel.setIndex(index);”的时候
     * 【注意】匿名函数的Integer input指mIndex.value_setter()的入参index，而String类型的返回值会影响mText.value_getter()
     * Transformations.map()方法体现了一种约定，一种事件执行之间关联关系
     */
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    /**
     * 方法调用地点：fragment.onCreate()。
     *
     * @param index 序号
     */
    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    /**
     * 方法调用地点：fragment.onCreateView()。
     * 【注意】上述调用地点主要添加【监视函数】，真正的执行时机除了手动显示调用，还有就是在index_setter()的时候
     * 而一旦监听到 text_getter()发生变化，则马上影响【UI显示】
     *
     * @return LiveData mText
     */
    public LiveData<String> getText() {
        return mText;
    }
}