package com.hfut.imlibrary;

import android.app.Application;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

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
    public static void init(Application context){
        EMOptions options = new EMOptions();
        EMClient.getInstance().init(context, options);
    }

    /**
     * 1. 创建房间，加入房间。
     * 2. 发送消息。
     */
}
