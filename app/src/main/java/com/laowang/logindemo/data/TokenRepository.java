package com.laowang.logindemo.data;

import androidx.fragment.app.Fragment;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.TCC;
import com.laowang.logindemo.data.model.TokenType;
import com.laowang.logindemo.ui.MyViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenRepository {

    private static volatile TokenRepository instance;

    private TokenDataSource dataSource;
    private MyViewModel myViewModel;

    public TokenDataSource getDataSource() {
        return dataSource;
    }

    public MyViewModel getMyViewModel() {
        return myViewModel;
    }

    private Map<String, List<KCT>> kctMap = new HashMap<>();

    private Map<String, List<TCC>> tccMap = new HashMap<>();

    private TokenRepository(TokenDataSource dataSource, Fragment fragment) {
        this.dataSource = dataSource;
        this.myViewModel = this.dataSource.getMyViewModel();
        this.myViewModel.getmKcts().observe(fragment.getViewLifecycleOwner(), kcts -> {
            if (kcts == null) return;
            if (kcts.size() > 0) {
                kctMap.put(this.meterStr, kcts);
                this.myViewModel.setmKctResult(new Result.Success<List<KCT>>(kcts, "查到了", R.string.result_success_kct_meter_str));
            } else {
                this.myViewModel.setmKctResult(new Result.Error("没查到", R.string.result_fail_kct_meter_str));
            }
        });
        this.myViewModel.getmTccs().observe(fragment.getViewLifecycleOwner(), tccs -> {
            if (tccs == null) return;
            if (tccs.size() > 0) {
                tccMap.put(this.meterStr, tccs);
                this.myViewModel.setmTccResult(new Result.Success<List<TCC>>(tccs, "查到了", R.string.result_success_tcc_meter_str));
            } else {
                this.myViewModel.setmTccResult(new Result.Error("没查到", R.string.result_fail_tcc_meter_str));
            }
        });
    }

    public static TokenRepository getInstance(TokenDataSource dataSource, Fragment fragment) {
        if (instance == null) {
            instance = new TokenRepository(dataSource, fragment);
        }
        return instance;
    }

    public boolean isCachedTokens(String meterNumber) {
        return kctMap.containsKey(meterNumber) || tccMap.containsKey(meterNumber);
    }

    public boolean isCachedTokens(String meterNumber, TokenType tokenType) {
        if (tokenType == TokenType.KCT) {
            return kctMap.containsKey(meterNumber);
        }
        if (tokenType == TokenType.TCC) {
            return tccMap.containsKey(meterNumber);
        }
        return false;
    }

    public Map<String, List<KCT>> getKctMap() {
        return kctMap;
    }

    public Map<String, List<TCC>> getTccMap() {
        return tccMap;
    }

    private String meterStr;

    public void queryKctsByMeterStr(String meterStr) {
        this.meterStr = meterStr;
        dataSource.queryTokenByTypeAndMeterStr(TokenType.KCT, meterStr);
//        Result<List<KCT>>
    }

    public void queryTccsByMeterStr(String meterStr) {
        this.meterStr = meterStr;
        dataSource.queryTokenByTypeAndMeterStr(TokenType.TCC, meterStr);
//        Result<List<TCC>>
    }
}
