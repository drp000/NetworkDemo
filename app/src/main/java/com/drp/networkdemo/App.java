package com.drp.networkdemo;


import android.app.Application;

import com.drp.base.IAppInfo;
import com.drp.base.model.BasicDataPreferenceUtil;
import com.drp.network.base.INetworkRequiredInfo;
import com.drp.network.base.NetworkApi;

/**
 *     ┌─┐       ┌─┐
 *  ┌──┘ ┴───────┘ ┴──┐
 *  │                 │
 *  │       ───       │
 *  │  ─┬┘       └┬─  │
 *  │                 │
 *  │       ─┴─       │
 *  │                 │
 *  └───┐         ┌───┘
 *      │         │
 *      │         │
 *      │         │
 *      │         └──────────────┐
 *      │                        │
 *      │                        ├─┐
 *      │                        ┌─┘
 *      │                        │
 *      └─┐  ┐  ┌───────┬──┐  ┌──┘
 *        │ ─┤ ─┤       │ ─┤ ─┤
 *        └──┴──┘       └──┴──┘
 * 神兽坐镇，永无bug！
 * @author DRP
 * @date 2021/4/23
 *
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        IAppInfo appInfo = new AppInfo(this);
        BasicDataPreferenceUtil.init(appInfo);

        NetworkApi.init(new INetworkRequiredInfo() {
            @Override
            public String getAppVersionName() {
                return BuildConfig.VERSION_NAME;
            }

            @Override
            public String getAppVersionCode() {
                return "" + BuildConfig.VERSION_CODE;
            }

            @Override
            public boolean isDebug() {
                return BuildConfig.DEBUG;
            }

            @Override
            public Application getApplicationContext() {
                return App.this;
            }
        });
    }
}