package com.laowang.logindemo.ui.token;

import android.content.Context;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TokenReadViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<TokenResult> mTokenResult = new MutableLiveData<>();
    /**
     * 表行，key-SN序号，TableRow 携带一条token数据
     */
    private final MutableLiveData<Map<Integer, TableRow>> mTableRows;

    private final TokenRepository tokenRepository = TokenRepository.getInstance(new TokenDataSource());

    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    public TokenReadViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is token-read fragment");
        mTableRows = new MutableLiveData<>();
        mTableRows.setValue(new TreeMap<>());
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

    /**
     * 设置结果消息，更新 页面数据（更新UI显示是别的方法）
     *
     * @param context  上下文
     * @param type     token 类型
     * @param meterStr meter 字符串
     */
    public void queryTokens(@NonNull Context context, @NonNull TokenType type, @NonNull String meterStr) {
        if (meterStr.trim().equals("")) {
            // 权限控制,普通用户不允许 表号为空，管理员表号空时候查询所有该类型的tokens
            if (loginRepository.getUser().getLevel().contains("0")) {
                mTokenResult.setValue(new TokenResult(null, R.string.result_fail_blank_meter_str));
            }
        }
        // TODO 先从 tokenRepository 缓存中查询
        // 成功不成功都得更新 table rows 数据
        TreeMap<Integer,TableRow> treeMap = new TreeMap<>();
        // 添加表头
        treeMap.put(0,addTableHeadInfoInRow(context));
        if (type == TokenType.KCT) {
            Result<List<KCT>> listResult = tokenRepository.queryKctsByMeterStr(meterStr);
            if (listResult instanceof Result.Success) {
                Result.Success<List<KCT>> result = (Result.Success<List<KCT>>) listResult;
                // 设置结果
                mTokenResult.setValue(new TokenResult(result.getCode(), null, null, null));
                // 填充 tree map
                List<KCT> data = result.getData();
                for (int i = 0; i < data.size(); i++) {
                    int sn = i + 1;
                    TableRow tableRow = addTokenInfoInRow(context, data.get(i), sn);
                    treeMap.put(sn, tableRow);
                }
            } else {
                Result.Error result = (Result.Error) listResult;
                mTokenResult.setValue(new TokenResult(null, result.getCode(), null, null));
            }
        } else {
            Result<List<TCC>> listResult = tokenRepository.queryTccsByMeterStr(meterStr);
            if (listResult instanceof Result.Success) {
                Result.Success<List<TCC>> result = (Result.Success<List<TCC>>) listResult;
                mTokenResult.setValue(new TokenResult(null, null, result.getCode(), null));
                List<TCC> data = result.getData();
                for (int i = 0; i < data.size(); i++) {
                    int sn = i + 1;
                    TableRow tableRow = addTokenInfoInRow(context, data.get(i), sn);
                    treeMap.put(sn, tableRow);
                }
            } else {
                Result.Error result = (Result.Error) listResult;
                mTokenResult.setValue(new TokenResult(null, null, null, result.getCode()));
            }
        }
        // 重新设置，以便观察到变化后更新UI
        mTableRows.setValue(treeMap);
    }

    /**
     * 将token信息填充进 TableRow对象
     *
     * @param context      上下文
     * @param token        token
     * @param serialNumber 表中的序号
     * @return Table Row
     */
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

    /**
     * 将 表头 填充进 TableRow对象
     *
     * @param context      上下文
     * @return Table Row
     */
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