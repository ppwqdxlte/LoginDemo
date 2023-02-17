package com.laowang.logindemo.ui.home;

import android.app.Activity;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.MainActivity;
import com.laowang.logindemo.R;
import com.laowang.logindemo.util.ResourceProvider;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue(ResourceProvider.getString(R.string.default_home_text));
    }

    public LiveData<String> getText() {
        return mText;
    }
}