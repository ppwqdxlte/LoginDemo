package com.laowang.logindemo.ui.token;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.LoginDataSource;
import com.laowang.logindemo.data.LoginRepository;
import com.laowang.logindemo.data.Result;
import com.laowang.logindemo.data.TokenDataSource;
import com.laowang.logindemo.data.TokenRepository;
import com.laowang.logindemo.data.model.BaseToken;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.TCC;
import com.laowang.logindemo.data.model.TokenType;
import com.laowang.logindemo.ui.MyViewModel;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TokenReadViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<TokenResult> mTokenResult = new MutableLiveData<>();

    private final MutableLiveData<Map<Integer, TableRow>> mTableRows;

    private TokenRepository tokenRepository;
    private MyViewModel myViewModel;
    public static Fragment fragment;

    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    public TokenReadViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is token-read fragment");
        mTableRows = new MutableLiveData<>();
        mTableRows.setValue(new TreeMap<>());
        tokenRepository = TokenRepository.getInstance(new TokenDataSource(fragment), fragment);
        this.myViewModel = tokenRepository.getMyViewModel();
        this.myViewModel.getmKctResult().observe(fragment.getViewLifecycleOwner(), listResult -> {
            if (listResult == null) return;
            if (listResult instanceof Result.Success) {
                Result.Success<List<KCT>> result = (Result.Success<List<KCT>>) listResult;
                mTokenResult.setValue(new TokenResult(result.getCode(), null, null, null));
                List<KCT> data = result.getData();
                for (int i = 0; i < data.size(); i++) {
                    int sn = i + 1;
                    TableRow tableRow = addTokenInfoInRow(this.context, data.get(i), sn);
                    this.treeMap.put(sn, tableRow);
                }
            } else {
                Result.Error result = (Result.Error) listResult;
                mTokenResult.setValue(new TokenResult(null, result.getCode(), null, null));
            }
            mTableRows.setValue(this.treeMap);
        });
        this.myViewModel.getmTccResult().observe(fragment.getViewLifecycleOwner(), listResult -> {
            if (listResult == null) return;
            if (listResult instanceof Result.Success) {
                Result.Success<List<TCC>> result = (Result.Success<List<TCC>>) listResult;
                mTokenResult.setValue(new TokenResult(null, null, result.getCode(), null));
                List<TCC> data = result.getData();
                for (int i = 0; i < data.size(); i++) {
                    int sn = i + 1;
                    TableRow tableRow = addTokenInfoInRow(this.context, data.get(i), sn);
                    this.treeMap.put(sn, tableRow);
                }
            } else {
                Result.Error result = (Result.Error) listResult;
                mTokenResult.setValue(new TokenResult(null, null, null, result.getCode()));
            }
            mTableRows.setValue(this.treeMap);
        });
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<TokenResult> getmTokenResult() {
        return mTokenResult;
    }

    public LiveData<Map<Integer, TableRow>> getmTableRows() {
        return mTableRows;
    }

    public void setmTokenResult(TokenResult tokenResult) {
        this.mTokenResult.setValue(tokenResult);
    }

    private Context context;
    private TreeMap<Integer, TableRow> treeMap;

    public void queryTokens(@NonNull Context context, @NonNull TokenType type, @NonNull String meterStr) {
        this.context = context;
        if (meterStr.trim().equals("")) {
            if (loginRepository.getUser().getLevel().contains("0")) {
                mTokenResult.setValue(new TokenResult(null, R.string.result_fail_blank_meter_str));
            }
        }
        // TODO 先从 tokenRepository 缓存中查询
        treeMap = new TreeMap<>();
        treeMap.put(0, addTableHeadInfoInRow(context));
        if (type == TokenType.KCT) {
            tokenRepository.queryKctsByMeterStr(meterStr);
        } else {
            tokenRepository.queryTccsByMeterStr(meterStr);
        }
    }

    private TableRow addTokenInfoInRow(Context context, BaseToken token, int serialNumber) {
        TableRow tokenRow = new TableRow(context);
        TextView sn = new TextView(context);
        sn.setText(serialNumber + "");
        TextView meterNo = new TextView(context);
        meterNo.setText(token.getMeterNo());
        meterNo.setWidth(360);
        TextView tokenStr = new TextView(context);
        tokenStr.setText(token.getToken());
        tokenRow.addView(sn);
        tokenRow.addView(meterNo);
        tokenRow.addView(tokenStr);
        return tokenRow;
    }

    private TableRow addTableHeadInfoInRow(Context context) {
        TableRow tokenRow = new TableRow(context);
        TextView sn = new TextView(context);
        sn.setText("SN");
        TextView meterNo = new TextView(context);
        meterNo.setText("Meter Number");
        meterNo.setWidth(360);
        TextView tokenStr = new TextView(context);
        tokenStr.setText("Token");
        tokenRow.addView(sn);
        tokenRow.addView(meterNo);
        tokenRow.addView(tokenStr);
        return tokenRow;
    }
}