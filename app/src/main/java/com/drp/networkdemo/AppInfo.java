package com.drp.networkdemo;


import android.app.Application;

import com.drp.base.IAppInfo;

/**
 * @author durui
 * @date 2021/4/2
 * @description
 */
public class AppInfo implements IAppInfo {

    private Application application;

    public AppInfo(Application application) {
        this.application = application;
    }

    @Override
    public boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    @Override
    public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    public Application getApplicationContext() {
        return application;
    }
}