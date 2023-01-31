package com.laowang.logindemo.ui.token;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TokenReadViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TokenReadViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}