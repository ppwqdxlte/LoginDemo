package com.laowang.logindemo.ui.management;

import android.util.Log;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tab页签的页面区视图模型
 */
public class PageViewModel extends ViewModel {
    /*
    * 首次进管理页，设置tab1增加1轮，设置tab2再增加1轮，都变成了2,
    * 但是以后在进入此管理页，就不再自动增加了，setIndex()和getText()在PlaceholderFragment中只调用2次
    *  */
    private static AtomicInteger mTextApplyedTimes = new AtomicInteger(0);
    private static AtomicInteger mTextGetterTimes = new AtomicInteger(0);
    private static AtomicInteger mIndexSetterTimes = new AtomicInteger(0);

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            Log.e("mTextApplyedTimes",mTextApplyedTimes.incrementAndGet()+"");
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        Log.e("mIndexSetterTimes",mIndexSetterTimes.incrementAndGet()+"");
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        Log.e("mTextGetterTimes",mTextGetterTimes.incrementAndGet()+"");
        return mText;
    }
}