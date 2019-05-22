package com.hfut.trouble;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.Nullable;

import com.hfut.base.activity.BaseActivity;
import com.hfut.imlibrary.IMManager;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;

import java.lang.ref.WeakReference;

import xiaoma.com.bomb.BmobManager;

public class SplashActivity extends BaseActivity {
    private MyHandler myHandler;
    private BusinessRunnable businessRunnable;

    @Override
    public int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化Bmob服务
        BmobManager.getInstance().init(this);

        myHandler = new MyHandler(this);
        businessRunnable = new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                if (IMManager.getInstance().isLogin()) {
                    //启动到MainActivity
                    myHandler.sendEmptyMessage(MyHandler.MAIN_WHAT);
                } else {
                    //跳转到LoginActivity
                    myHandler.sendEmptyMessage(MyHandler.LOGIN_WHAT);
                }
            }
        };
        ThreadDispatcher.getInstance().postBusinessRunnableDelayed(businessRunnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ThreadDispatcher.getInstance().removeBusinessRunnable(businessRunnable);
    }

    private static class MyHandler extends Handler {
        private WeakReference<SplashActivity> splashActivityWeakReference;
        private static final int LOGIN_WHAT = 0;
        private static final int MAIN_WHAT = 1;
        private MyHandler(SplashActivity splashActivity){
            this.splashActivityWeakReference = new WeakReference<>(splashActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SplashActivity splashActivity = splashActivityWeakReference.get();
            if(splashActivity == null){
                return;
            }
            switch (msg.what){
                case LOGIN_WHAT:
                    splashActivity.startActivity(new Intent(splashActivity, LoginActivity.class));
                    splashActivity.finish();
                    break;
                case MAIN_WHAT:
                    splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                    splashActivity.finish();
                    break;
            }
        }
    }
}
