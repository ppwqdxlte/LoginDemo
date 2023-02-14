package com.laowang.logindemo.data;

import com.laowang.logindemo.R;
import com.laowang.logindemo.data.model.BaseToken;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.TCC;
import com.laowang.logindemo.data.model.TokenType;
import com.laowang.logindemo.util.ResourceProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenRepository {

    private static volatile TokenRepository instance;

    private TokenDataSource dataSource;

    private Map<String, List<KCT>> kctMap = new HashMap<>();

    private Map<String, List<TCC>> tccMap = new HashMap<>();

    private TokenRepository(TokenDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static TokenRepository getInstance(TokenDataSource dataSource) {
        if (instance == null) {
            instance = new TokenRepository(dataSource);
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

    public Result<List<KCT>> queryKctsByMeterStr(String meterStr) {
        List<KCT> list = dataSource.queryTokenByTypeAndMeterStr(TokenType.KCT, meterStr);
        if (list.size() > 0) {
            kctMap.put(meterStr, list);
            return new Result.Success<List<KCT>>(list, "查到了", R.string.result_success_kct_meter_str);
        }
        return new Result.Error("没查到", R.string.result_fail_kct_meter_str);
    }

    public Result<List<TCC>> queryTccsByMeterStr(String meterStr) {
        List<TCC> list = dataSource.queryTokenByTypeAndMeterStr(TokenType.TCC, meterStr);
        if (list.size() > 0) {
            tccMap.put(meterStr, list);
            return new Result.Success<List<TCC>>(list, "查到了", R.string.result_success_tcc_meter_str);
        }
        return new Result.Error("没查到", R.string.result_fail_tcc_meter_str);
    }
}
