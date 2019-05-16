package com.hfut.imlibrary;

import android.app.Application;
import android.text.TextUtils;

import com.hfut.imlibrary.event.CreateGroupEvent;
import com.hfut.imlibrary.event.DestroyGroup;
import com.hfut.imlibrary.event.ExitGroupEvent;
import com.hfut.imlibrary.event.JoinGroupEvent;
import com.hfut.imlibrary.event.LoginEvent;
import com.hfut.imlibrary.event.LogoutEvent;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.event.RegisterEvent;
import com.hfut.imlibrary.event.SendMessageEvent;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * IM工具类
 *
 * @author cms
 */
public class IMManager {
    private MyListener myListener;
    private static IMManager instance = new IMManager();

    private IMManager() {
    }

    public static IMManager getInstance() {
        return instance;
    }

    /**
     * 使用前必须先init
     *
     * @param context
     */
    public void init(Application context) {
        EMOptions options = new EMOptions();
        EMClient.getInstance().init(context, options);
    }

    /**
     * 注册
     *
     * @param userName 环信做的限制：必须小写字母
     * @param password
     * @return
     */
    public void register(String userName, String password) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            EventBus.getDefault().post(new RegisterEvent(false));
            return;
        }
        try {
            EMClient.getInstance().createAccount(userName, password);
            EventBus.getDefault().post(new RegisterEvent(true));
        } catch (HyphenateException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new RegisterEvent(false));
        }
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     */
    public void login(String userName, String password) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            EventBus.getDefault().post(new LoginEvent(false));
            return;
        }
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //开始监听接收消息事件
                myListener = new MyListener();
                EMClient.getInstance().chatManager().addMessageListener(myListener);
                EventBus.getDefault().post(new LoginEvent(true));
            }

            @Override
            public void onError(int code, String error) {
                EventBus.getDefault().post(new LoginEvent(false));
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    /**
     * 登出
     */
    public void logout() {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().removeMessageListener(myListener);
                EventBus.getDefault().post(new LogoutEvent(true));
            }

            @Override
            public void onError(int code, String error) {
                EventBus.getDefault().post(new LogoutEvent(false));
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    /**
     * 创建群组
     */
    public void createGroup(String groupName) {
        EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
        options.maxUsers = 10;
        //设置创建的群组所有人都可以无条件加入
        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
        try {
            EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(groupName, "", new String[]{}, "", options);
            EventBus.getDefault().post(new CreateGroupEvent(emGroup.getGroupId(), true));
        } catch (HyphenateException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new CreateGroupEvent(null, false));
        }
    }

    /**
     * 加入群组
     *
     * @param groupId
     */
    public void joinGroup(String groupId) {
        try {
            EMClient.getInstance().groupManager().joinGroup(groupId);
            EventBus.getDefault().post(new JoinGroupEvent(true));
        } catch (HyphenateException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new JoinGroupEvent(false));
        }
    }

    /**
     * 发送群组消息
     *
     * @param content 发送消息的内容
     * @param groupId 发送消息的群组id
     */
    public void sendGroupMessage(String content, String groupId) {
        EMMessage message = EMMessage.createTxtSendMessage(content, groupId);
        message.setChatType(EMMessage.ChatType.GroupChat);

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                EventBus.getDefault().post(new SendMessageEvent(true));
            }

            @Override
            public void onError(int code, String error) {
                EventBus.getDefault().post(new SendMessageEvent(false));
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }


    /**
     * 解散群组，只有创建者才能解散群组
     *
     * @param groupId
     */
    public void destroyGroup(String groupId) {
        try {
            EMClient.getInstance().groupManager().destroyGroup(groupId);
            EventBus.getDefault().post(new DestroyGroup(true));
        } catch (HyphenateException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new DestroyGroup(false));
        }
    }

    /**
     * 退出群组
     *
     * @param groupId
     */
    public void exitGroup(String groupId) {
        try {
            EMClient.getInstance().groupManager().leaveGroup(groupId);
            EventBus.getDefault().post(new ExitGroupEvent(true));
        } catch (HyphenateException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new ExitGroupEvent(false));
        }
    }

    private class MyListener implements EMMessageListener{
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            EventBus.getDefault().post(new MessageReceivedEvent(true));
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }
        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    }
}
