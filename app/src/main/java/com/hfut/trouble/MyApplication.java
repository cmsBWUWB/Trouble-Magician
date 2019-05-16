package com.hfut.trouble;

import android.app.Application;

import com.hfut.imlibrary.IMManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 IM 服务
        IMManager.getInstance().init(this);

    }
}
