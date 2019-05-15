package com.hfut.imlibrary;

import android.app.Application;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

/**
 * IM工具类
 */
public class IMUtils {
    private static IMUtils instance = new IMUtils();


    private IMUtils(){}
    public static IMUtils getInstance(){
        //todo
        return instance;
    }

    /**
     * 使用前必须先init
     * @param context
     */
    public void init(Application context){
        EMOptions options = new EMOptions();
        EMClient.getInstance().init(context, options);
    }

    /**
     * 1. 创建房间，加入房间。
     * 2. 发送消息。
     */

    public class RegisterEvent{
        private boolean success;
        RegisterEvent(boolean success){
            this.success = success;
        }
        public boolean isSuccess(){
            return success;
        }
    }
    /**
     * 注册
     * @param userName 环信做的限制：必须小写字母
     * @param password
     * @return
     */
    public void register(String userName, String password){
        try {
            EMClient.getInstance().createAccount(userName, password);
            EventBus.getDefault().post(new RegisterEvent(true));
        } catch (HyphenateException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new RegisterEvent(false));
        }
    }

    public class LoginEvent{
        private boolean success;
        LoginEvent(boolean success){
            this.success = success;
        }
        public boolean isSuccess(){
            return success;
        }
    }

    /**
     * 登录
     * @param userName
     * @param password
     */
    public void login(String userName, String password){
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new LoginEvent(true));
            }

            @Override
            public void onError(int code, String error) {
                EventBus.getDefault().post(new LoginEvent(false));
            }

            @Override
            public void onProgress(int progress, String status) { }
        });
    }
}
