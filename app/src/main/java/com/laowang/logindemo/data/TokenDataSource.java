package com.laowang.logindemo.data;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.data.model.BaseToken;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.TCC;
import com.laowang.logindemo.data.model.TokenType;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenDataSource<T> {

    private RestfulApiHandler apiHandler = new RestfulApiHandler();

    public List<BaseToken<T>> queryTokenByTypeAndMeterStr(T type, String meterStr) {
        List<BaseToken<T>> tokens = new ArrayList<>();
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_TYPE_METER;
        Map<String, String> params = new HashMap<>();
        params.put("tokenType", type.getClass().getSimpleName());
        params.put("meterStr", meterStr);
        Result<String>[] result = apiHandler.postSync(null, strUrl, params);
        while (result[0] == null) {
        }
        if (result[0] instanceof Result.Success) {
            Result.Success<String> success = (Result.Success<String>) result[0];
            String jsonStr = success.getData();
            com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
            if (fromJson.getData() == null) {
                return tokens;
            }
            List<LinkedTreeMap> data = (ArrayList<LinkedTreeMap>) fromJson.getData();
            for (int i = 0; i < data.size(); i++) {
                LinkedTreeMap treeMap = data.get(i);
                BaseToken token = null;
                if (type == TokenType.KCT) {
                    token = new KCT(
                            treeMap.get("batchNo").toString(),
                            treeMap.get("meterNo").toString(),
                            treeMap.get("token").toString(),
                            treeMap.get("date").toString());
                } else {
                    token = new TCC(
                            treeMap.get("batchNo").toString(),
                            treeMap.get("meterNo").toString(),
                            treeMap.get("token").toString(),
                            treeMap.get("date").toString());
                }
                tokens.add(token);
            }
        }
        return tokens;
    }
}
