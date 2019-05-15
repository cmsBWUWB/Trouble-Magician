package com.hfut.trouble;

import android.app.Application;

import com.hfut.imlibrary.IMUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化 IM 服务
        IMUtils.getInstance().init(this);
    }
}
