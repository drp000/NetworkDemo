package com.drp.base.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.drp.base.IAppInfo;

/**
 * @author durui
 * @date 2021/4/2
 * @description
 */
public class BasicDataPreferenceUtil {

    private static final String SP_NAME = "cached";
    private static IAppInfo APP_INFO;
    private SharedPreferences sharedPreferences;

    private BasicDataPreferenceUtil() {
        sharedPreferences = APP_INFO.getApplicationContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    private static class Holder {

        private static final BasicDataPreferenceUtil instance = new BasicDataPreferenceUtil();
    }

    public static BasicDataPreferenceUtil getInstance() {
        return Holder.instance;
    }

    public static void init(IAppInfo appInfo) {
        APP_INFO = appInfo;
    }

    public void setString(String mCachedPreferenceKey, String data) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(mCachedPreferenceKey, data);
        edit.apply();
    }

    public String getString(String mCachedPreferenceKey) {
        return sharedPreferences.getString(mCachedPreferenceKey, null);
    }
}