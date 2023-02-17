package com.laowang.logindemo.util;

import android.content.Context;
import android.util.Log;

public class ResourceProvider {

    private static Context mCtx;

    public ResourceProvider(Context context) {
        this.mCtx = context;
    }

    public String getStr(int resNum) {
        return getString(resNum);
    }

    public static String getString(int resNum){
        if (mCtx == null){
            try {
                throw new Exception("Context对象尚未初始化，无法获取上下文资源！");
            } catch (Exception e) {
                Log.e(e.getMessage(),"请调用ResourceProvider.setCtx(Context context)方法初始化mCtx！");
                return null;
            }
        } else {
            return mCtx.getString(resNum);
        }
    }

    public static void setmCtx(Context context){
        mCtx = context;
    }

}
