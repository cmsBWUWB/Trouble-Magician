package com.hfut.imlibrary;

import android.app.Application;
import android.text.TextUtils;

import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.model.Group;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * IM工具类
 *
 * @author cms
 */
public class IMManager {
    private MessageReceivedListener messageReceivedListener;
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
    public boolean register(String userName, String password) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return false;
        }
        try {
            EMClient.getInstance().createAccount(userName, password);
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     */
    public void login(String userName, String password, final OperateCallBack callBack) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            callBack.onFailure();
            return;
        }
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                //开始监听接收消息事件
                messageReceivedListener = new MessageReceivedListener();
                EMClient.getInstance().chatManager().addMessageListener(messageReceivedListener);
                callBack.onSuccess();
            }

            @Override
            public void onError(int code, String error) {
                callBack.onFailure();
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    /**
     * 登出
     */
    public void logout(final OperateCallBack callBack) {
        EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().chatManager().removeMessageListener(messageReceivedListener);
                callBack.onSuccess();
            }

            @Override
            public void onError(int code, String error) {
                callBack.onFailure();
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    /**
     * 创建群组
     */
    public boolean createGroup(String groupName) {
        EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
        options.maxUsers = 10;
        //设置创建的群组所有人都可以无条件加入
        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
        try {
            EMGroup emGroup = EMClient.getInstance().groupManager().createGroup(groupName, "", new String[]{}, "", options);
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 加入群组
     *
     * @param groupId
     */
    public boolean joinGroup(String groupId) {
        try {
            EMClient.getInstance().groupManager().joinGroup(groupId);
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 解散群组，只有创建者才能解散群组
     *
     * @param groupId
     */
    public boolean destroyGroup(String groupId) {
        try {
            EMClient.getInstance().groupManager().destroyGroup(groupId);
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 退出群组
     *
     * @param groupId
     */
    public boolean exitGroup(String groupId) {
        try {
            EMClient.getInstance().groupManager().leaveGroup(groupId);
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 发送群组消息
     *
     * @param content 发送消息的内容
     * @param groupId 发送消息的群组id
     */
    public void sendGroupMessage(String content, String groupId, final OperateCallBack callBack) {
        EMMessage message = EMMessage.createTxtSendMessage(content, groupId);
        message.setChatType(EMMessage.ChatType.GroupChat);

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                callBack.onSuccess();
            }

            @Override
            public void onError(int code, String error) {
                callBack.onFailure();
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    private List<String> getGroupMember(String groupId) {
        List<String> memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        final int pageSize = 20;
        //如果群成员较多，需要多次从服务器获取完成
        //TODO 环信sdk里面的参考代码有问题！！
        do {
            try {
                result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,
                        result != null ? result.getCursor() : "", pageSize);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            if(result != null)
                memberList.addAll(result.getData());
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
        return memberList;
    }

    public List<Group> getAllGroupAndMember() {
        List<EMGroup> groupList = new ArrayList<>();
        List<Group> result = new ArrayList<>();
        try {
            groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
            for(EMGroup emGroup:groupList){
                emGroup.getMembers().addAll(getGroupMember(emGroup.getGroupId()));
            }
            result = emGroupList2GroupList(groupList);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Group> emGroupList2GroupList(List<EMGroup> emGroupList) {
        List<Group> groupList = new ArrayList<>();
        for (EMGroup emGroup : emGroupList) {
            groupList.add(emGroup2Group(emGroup));
        }
        return groupList;
    }
    private Group emGroup2Group(EMGroup emGroup){
        String groupId = emGroup.getGroupId();
        String owner = emGroup.getOwner();
        List<String> memberList = emGroup.getMembers();
        return new Group(groupId, owner, memberList);
    }

    private class MessageReceivedListener implements EMMessageListener {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            EventBus.getDefault().post(new MessageReceivedEvent(messages));
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
