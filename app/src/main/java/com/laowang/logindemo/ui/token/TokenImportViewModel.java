package com.laowang.logindemo.ui.token;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TokenImportViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TokenImportViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is token-import fragment.");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
