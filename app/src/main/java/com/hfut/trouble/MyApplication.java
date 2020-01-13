package com.hfut.trouble;

import android.app.Application;

import com.hfut.base.application.CoreManager;
import com.hfut.base.manager.IMManager;
import com.hfut.utils.utils.log.LogPrint;
import com.hfut.utils.utils.Utils;
import com.hfut.voice.VoiceManager;

import xiaoma.com.bomb.BmobManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化存储Application的Context
        CoreManager.init(this);
        //初始化Bmob服务
        BmobManager.getInstance().init(this);
        // 初始化 IM 服务
        IMManager.getInstance().init(this);
        //初始化日志模块
        LogPrint.init(Utils.isDebuggable(this));
        //初始化语音
        VoiceManager.INSTANCE.init(this);
    }
}
