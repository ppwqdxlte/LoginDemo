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
    /**
     * 缓存KCT类型的tokens，key-表号，value-令牌
     */
    private Map<String, List<KCT>> kctMap = new HashMap<>();
    /**
     * 缓存TCC类型的tokens，key-表号，value-令牌
     */
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

    /**
     * 不确定表号对应的token类型
     *
     * @param meterNumber 表号
     * @return 是否缓存了表号拥有的Tokens
     */
    public boolean isCachedTokens(String meterNumber) {
        return kctMap.containsKey(meterNumber) || tccMap.containsKey(meterNumber);
    }

    /**
     * 确定表号对应的token类型
     *
     * @param meterNumber 表号
     * @param tokenType   token类型
     * @return 是否缓存了表号拥有的Tokens
     */
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

    /* 以下均为业务代码 */

    /**
     * 对查询结果进行缓存,装饰成功码或失败码
     *
     * @param meterStr 表号
     * @return 携带token列表的结果
     */
    public Result<List<KCT>> queryKctsByMeterStr(String meterStr) {
        // handle query
        List<KCT> list = dataSource.queryTokenByTypeAndMeterStr(TokenType.KCT, meterStr);
        if (list.size() > 0) {
            // cache the result
            kctMap.put(meterStr, list);
            return new Result.Success<List<KCT>>(list, "查到了", R.string.result_success_kct_meter_str);
        }
        return new Result.Error("没查到", R.string.result_fail_kct_meter_str);
    }

    /**
     * 对查询结果进行缓存,装饰成功码或失败码
     *
     * @param meterStr 表号
     * @return 携带token列表的结果
     */
    public Result<List<TCC>> queryTccsByMeterStr(String meterStr) {
        // handle query
        List<TCC> list = dataSource.queryTokenByTypeAndMeterStr(TokenType.TCC, meterStr);
        if (list.size() > 0) {
            // cache the result
            tccMap.put(meterStr, list);
            return new Result.Success<List<TCC>>(list, "查到了", R.string.result_success_tcc_meter_str);
        }
        return new Result.Error("没查到", R.string.result_fail_tcc_meter_str);
    }
}
