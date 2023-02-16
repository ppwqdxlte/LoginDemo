package com.laowang.logindemo.data;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.laowang.logindemo.data.model.KCT;
import com.laowang.logindemo.data.model.TCC;
import com.laowang.logindemo.data.model.TokenType;
import com.laowang.logindemo.ui.MyViewModel;
import com.laowang.logindemo.util.RestfulApiHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TokenDataSource<T> {

    private RestfulApiHandler apiHandler = new RestfulApiHandler();

    private MyViewModel myViewModel;

    public MyViewModel getMyViewModel() {
        return myViewModel;
    }

    public TokenDataSource(Fragment fragment) {
        myViewModel = new ViewModelProvider(fragment).get(MyViewModel.class);
        myViewModel.getmResultString().observe(fragment.getViewLifecycleOwner(), stringResult -> {
            if (stringResult == null) return;
            List<KCT> kcts = new ArrayList<>();
            List<TCC> tccs = new ArrayList<>();
            if (stringResult instanceof Result.Success) {
                Result.Success<String> success = (Result.Success<String>) stringResult;
                String jsonStr = success.getData();
                com.laowang.logindemo.apientity.Result fromJson = new Gson().fromJson(jsonStr, com.laowang.logindemo.apientity.Result.class);
                if (fromJson.getData() != null) {
                    List<LinkedTreeMap> data = (ArrayList<LinkedTreeMap>) fromJson.getData();
                    for (int i = 0; i < data.size(); i++) {
                        LinkedTreeMap treeMap = data.get(i);
                        if (this.type == TokenType.KCT) {
                            KCT token = new KCT(
                                    treeMap.get("batchNo").toString(),
                                    treeMap.get("meterNo").toString(),
                                    treeMap.get("token").toString(),
                                    treeMap.get("date").toString());
                            kcts.add(token);
                        } else {
                            TCC token = new TCC(
                                    treeMap.get("batchNo").toString(),
                                    treeMap.get("meterNo").toString(),
                                    treeMap.get("token").toString(),
                                    treeMap.get("date").toString());
                            tccs.add(token);
                        }
                    }
                }
            }
            if (this.type == TokenType.KCT) {
                myViewModel.setmKcts(kcts);
            } else {
                myViewModel.setmTccs(tccs);
            }
        });
    }

    private T type;

    public void queryTokenByTypeAndMeterStr(T type, String meterStr) {
        this.type = type;
        String strUrl = ApiUrl.API_BASE + ApiUrl.API_TYPE_METER;
        Map<String, String> params = new HashMap<>();
        params.put("tokenType", type == TokenType.KCT ? "KCT" : "TCC");
        params.put("meterStr", meterStr);
        apiHandler.postAsync(myViewModel, strUrl, params);
    }
}
