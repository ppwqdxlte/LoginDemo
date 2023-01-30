package com.laowang.logindemo.util;

import android.content.Context;
import android.util.Log;

public class ResourceProvider {
    /**
     * mutable context成员变量，可修改
     */
    private static Context mCtx;

    public ResourceProvider(Context context) {
        this.mCtx = context;
    }
    /**
     * 通过资源号码获得上下文的字符串资源
     * @param resNum 资源值 eg. R.string.xxx
     * @return 字符串资源
     */
    public String getStr(int resNum) {
        return getString(resNum);
    }
    /**
     * 通过资源号码获得上下文的字符串资源，静态方法
     * @param resNum 资源值 eg. R.string.xxx
     * @return 字符串资源
     */
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
