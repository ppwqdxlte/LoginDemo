package com.laowang.logindemo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.TCC;

import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Result<String>> mResultString;

    private MutableLiveData<List<KCT>> mKcts;
    private MutableLiveData<List<TCC>> mTccs;

    private MutableLiveData<Result<List<KCT>>> mKctResult;
    private MutableLiveData<Result<List<TCC>>> mTccResult;

    public MyViewModel() {
        this.mResultString = new MutableLiveData<>();
        this.mKcts = new MutableLiveData<>();
        this.mTccs = new MutableLiveData<>();
        this.mKctResult = new MutableLiveData<>();
        this.mTccResult = new MutableLiveData<>();
    }

    public void setmResultString(Result<String> result) {
        // 如果setValue则报错 IllegalStateException: Cannot invoke setValue on a background thread
        this.mResultString.postValue(result);
    }

    public LiveData<Result<String>> getmResultString() {
        return mResultString;
    }

    public LiveData<List<KCT>> getmKcts() {
        return mKcts;
    }

    public void setmKcts(List<KCT> kcts) {
        this.mKcts.setValue(kcts);
    }

    public LiveData<List<TCC>> getmTccs() {
        return mTccs;
    }

    public void setmTccs(List<TCC> tccs) {
        this.mTccs.setValue(tccs);
    }

    public LiveData<Result<List<KCT>>> getmKctResult() {
        return mKctResult;
    }

    public void setmKctResult(Result<List<KCT>> kctResult) {
        this.mKctResult.setValue(kctResult);
    }

    public LiveData<Result<List<TCC>>> getmTccResult() {
        return mTccResult;
    }

    public void setmTccResult(Result<List<TCC>> tccResult) {
        this.mTccResult.setValue(tccResult);
    }
}
