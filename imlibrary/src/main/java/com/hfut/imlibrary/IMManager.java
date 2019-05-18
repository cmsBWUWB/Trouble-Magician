package com.hfut.imlibrary;

import android.app.Application;
import android.text.TextUtils;

import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.model.Group;
import com.hfut.imlibrary.model.Message;
import com.hfut.imlibrary.model.User;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 即时通讯
 * 注册、登录、登出
 * 获取：当前用户的群组列表、群成员
 * 搜索：群组、用户
 * 群组：创建、加入、退出、解散
 * 好友：添加、同意
 * 消息：发送、接收
 *
 * @author cms
 */
public class IMManager {
    private EMClient emClient;
    private EMChatManager emChatManager;
    private EMGroupManager emGroupManager;

    private MessageReceivedListener messageReceivedListener;

    private User currentLoginUser;


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
        emClient = EMClient.getInstance();
        emClient.init(context, options);
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
            emClient.createAccount(userName, password);
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
    public void login(final String userName, String password, final OperateCallBack callBack) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            callBack.onFailure();
            return;
        }
        emClient.login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                emChatManager = emClient.chatManager();
                emGroupManager = emClient.groupManager();

                currentLoginUser = new User(userName);
                //开始监听接收消息事件
                messageReceivedListener = new MessageReceivedListener();
                emChatManager.addMessageListener(messageReceivedListener);
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

    public User getCurrentLoginUser() {
        return currentLoginUser;
    }

    /**
     * 登出
     */
    public void logout(final OperateCallBack callBack) {
        emClient.logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                emChatManager.removeMessageListener(messageReceivedListener);
                currentLoginUser = null;
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
    public Group createGroup(String groupName, int maxUsers) {
        EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
        options.maxUsers = maxUsers;
        //设置创建的群组所有人都可以无条件加入
        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
        try {
            EMGroup emGroup = emGroupManager.createGroup(groupName, "",
                    new String[]{}, "", options);
            return Group.emGroup2Group(emGroup);
        } catch (HyphenateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加入群组
     *
     * @param groupId
     */
    public boolean joinGroup(String groupId) {
        try {
            emGroupManager.joinGroup(groupId);
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
            emGroupManager.destroyGroup(groupId);
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
            emGroupManager.leaveGroup(groupId);
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
        emChatManager.sendMessage(message);
    }

    /**
     * 从服务器上请求获取群组的全部成员
     *
     * @param groupId
     * @return
     */
    public List<User> requestGroupMember(String groupId) {
        List<String> memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        final int pageSize = 100;
        //如果群成员较多，需要多次从服务器获取完成
        do {
            try {
                result = emGroupManager.fetchGroupMembers(groupId,
                        result != null ? result.getCursor() : "", pageSize);
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
            if (result == null)
                break;
            memberList.addAll(result.getData());
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

        return User.usernameList2UserList(memberList);
    }
    /**
     * 从服务器上请求获取当前用户的群组列表
     *
     * @return
     */
    public List<Group> requestGroupList() {
        List<Group> result = new ArrayList<>();
        try {
            List<EMGroup> emGroupList = emGroupManager.getJoinedGroupsFromServer();
            result.addAll(Group.emGroupList2GroupList(emGroupList));
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return result;
    }
    /**
     * 搜索群组的基本信息
     * @return
     */
    public Group requestGroupInfo(String groupId){
        try {
            EMGroup emGroup = emGroupManager.getGroupFromServer(groupId);
            return Group.emGroup2Group(emGroup);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class MessageReceivedListener implements EMMessageListener {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息
            List<Message> messageList = new ArrayList<>();
            for(EMMessage emMessage:messages){
                messageList.add(Message.emMessage2Message(emMessage));
            }
            EventBus.getDefault().post(new MessageReceivedEvent(messageList));
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
