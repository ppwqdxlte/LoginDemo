package com.laowang.logindemo.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.ManagedUser;
import com.laowang.logindemo.data.model.TCC;

import java.util.List;

public class MyViewModel extends ViewModel {

    private MutableLiveData<Result<String>> mResultString;

    private MutableLiveData<List<KCT>> mKcts;
    private MutableLiveData<List<TCC>> mTccs;

    private MutableLiveData<Result<List<KCT>>> mKctResult;
    private MutableLiveData<Result<List<TCC>>> mTccResult;

    private MutableLiveData<Result<ManagedUser>> mCreateResult;
    private MutableLiveData<Result<ManagedUser>> mModifyResult;
    private MutableLiveData<Result<ManagedUser>> mDeleteResult;
    private MutableLiveData<Result<ManagedUser>> mChangeResult;

    public MyViewModel() {
        this.mResultString = new MutableLiveData<>();
        this.mKcts = new MutableLiveData<>();
        this.mTccs = new MutableLiveData<>();
        this.mKctResult = new MutableLiveData<>();
        this.mTccResult = new MutableLiveData<>();
        this.mCreateResult = new MutableLiveData<>();
        this.mModifyResult = new MutableLiveData<>();
        this.mDeleteResult = new MutableLiveData<>();
        this.mChangeResult = new MutableLiveData<>();
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

    public LiveData<Result<ManagedUser>> getmCreateResult() {
        return mCreateResult;
    }

    public void setmCreateResult(Result<ManagedUser> createResult) {
        this.mCreateResult.setValue(createResult);
    }

    public LiveData<Result<ManagedUser>> getmModifyResult() {
        return mModifyResult;
    }

    public void setmModifyResult(Result<ManagedUser> modifyResult) {
        this.mModifyResult.setValue(modifyResult);
    }

    public LiveData<Result<ManagedUser>> getmDeleteResult() {
        return mDeleteResult;
    }

    public void setmDeleteResult(Result<ManagedUser> deleteResult) {
        this.mDeleteResult.setValue(deleteResult);
    }

    public LiveData<Result<ManagedUser>> getmChangeResult() {
        return mChangeResult;
    }

    public void setmChangeResult(Result<ManagedUser> changeResult) {
        this.mChangeResult.setValue(changeResult);
    }
}
