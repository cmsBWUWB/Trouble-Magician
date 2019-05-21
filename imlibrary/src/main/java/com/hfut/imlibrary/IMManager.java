package com.hfut.imlibrary;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.hfut.imlibrary.listener.FriendChangeListener;
import com.hfut.imlibrary.listener.GroupChangeListener;
import com.hfut.imlibrary.listener.MessageReceivedListener;
import com.hfut.imlibrary.model.*;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMContactManager;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 聊天系统
 * <p>
 * 注册、登录、登出
 * 与服务器的连接是否断开
 * 同步与获取：当前用户的群组列表、群成员
 * 群组：创建、加入、退出、解散、拉人入群，申请入群
 * 好友：申请添加好友
 * 消息：发送
 *
 * @author cms
 */
public class IMManager {
    private static final String TAG = "IMManager";
    private EMClient emClient;
    private EMChatManager emChatManager;
    private EMGroupManager emGroupManager;
    private EMContactManager emContactManager;

    private MessageReceivedListener messageReceivedListener;
    private GroupChangeListener groupChangeListener;
    private FriendChangeListener friendChangeListener;

    /**
     * bug：存在线程安全的问题
     */
    private User currentLoginUser;
    private List<Group> groupList;
    private List<User> friendList;
    private List<Chat> chatList;


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

    public void setListener() {
        messageReceivedListener = new MessageReceivedListener();
        groupChangeListener = new GroupChangeListener();
        friendChangeListener = new FriendChangeListener(this.friendList);

        emChatManager.addMessageListener(messageReceivedListener);
        emGroupManager.addGroupChangeListener(groupChangeListener);
        emContactManager.setContactListener(friendChangeListener);
    }

    public void unsetListener() {
        emChatManager.removeMessageListener(messageReceivedListener);
        emGroupManager.removeGroupChangeListener(groupChangeListener);
        emContactManager.removeContactListener(friendChangeListener);
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

    public boolean isLogin() {
        return emClient.isLoggedInBefore();
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     */
    public void login(final String userName, String password,
                      final OperateCallBack callBack) {
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            callBack.onFailure();
            return;
        }
        emClient.login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                onLoginSuccess();
                callBack.onSuccess();
            }

            @Override
            public void onError(int code, String error) {
                Log.e(TAG, "onLoginError: " + error);
                callBack.onFailure();
            }

            @Override
            public void onProgress(int progress, String status) {
            }
        });
    }

    public void onLoginSuccess() {
        emChatManager = emClient.chatManager();
        emGroupManager = emClient.groupManager();
        emContactManager = emClient.contactManager();

        //登录成功则加载数据
        currentLoginUser = new User(emClient.getCurrentUser());
        initData();
        setListener();
    }

    private void onLogout() {
        unsetListener();
    }

    /**
     * 登出
     */
    public void logout(final OperateCallBack callBack) {
        if (!emClient.isLoggedInBefore()) {
            //如果未登录过或者已经注销了
            if (callBack != null)
                callBack.onSuccess();
            return;
        }
        emClient.logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                onLogout();
                if (callBack != null)
                    callBack.onSuccess();
            }

            @Override
            public void onError(int code, String error) {
                if (callBack != null)
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
            return IMUtils.emGroup2Group(emGroup);
        } catch (HyphenateException e) {
            e.printStackTrace();
            return null;
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
     * 申请加入群组
     *
     * @param groupId
     */
    public boolean requestJoinGroup(String groupId) {
        try {
            emGroupManager.joinGroup(groupId);
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
     * 拉人入群
     *
     * @param username
     * @param groupId
     * @return
     */
    public boolean inviteJoinGroup(String username, String groupId) {
        try {
            emGroupManager.inviteUser(groupId, new String[]{username}, "");
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 拉人入群
     *
     * @param username
     * @param groupId
     * @return
     */
    public boolean inviteJoinGroup(List<String> username, String groupId) {
        try {
            emGroupManager.inviteUser(groupId, (String[]) username.toArray(), "");
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 申请添加好友
     *
     * @param username
     * @return
     */
    public boolean requestAddFriend(String username) {
        try {
            emContactManager.addContact(username, "");
            Log.e(TAG, "requestAddFriend: success");
            return true;
        } catch (HyphenateException e) {
            e.printStackTrace();
            Log.e(TAG, "requestAddFriend: failed");
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
     * 发送好友消息
     *
     * @param content  发送消息的内容
     * @param username 发送消息的好友名
     */
    public void sendFriendMessage(String content, String username, final OperateCallBack callBack) {
        EMMessage message = EMMessage.createTxtSendMessage(content, username);
        message.setChatType(EMMessage.ChatType.Chat);

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


    public User getCurrentLoginUser() {
        return this.currentLoginUser;
    }

    public List<Group> getGroupList() {
        return new ArrayList<>(this.groupList);
    }

    public List<User> getFriendList() {
        return new ArrayList<>(this.friendList);
    }

    public List<Chat> getChatList() {
        return new ArrayList<>(this.chatList);
    }

    /**
     * 第一次从网络上获取所有数据
     */
    private void initData() {
        friendList = getFriendListFromNet();
        if (friendList != null) {
            friendList = Collections.synchronizedList(friendList);
        }
        chatList = getConversationList();
        if (chatList != null) {
            chatList = Collections.synchronizedList(chatList);
        }
        groupList = getGroupListFromNet();
        if (groupList != null) {
            groupList = Collections.synchronizedList(groupList);
        }
    }

    /**
     * 从网络上获取好友列表
     *
     * @return
     */
    private List<User> getFriendListFromNet() {
        try {
            List<String> usernameList = emContactManager.getAllContactsFromServer();
            List<User> result = new ArrayList<>();
            for (String username : usernameList) {
                result.add(new User(username));
            }
            return result;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取聊天会话列表
     *
     * @return
     */
    private List<Chat> getConversationList() {
        emChatManager.loadAllConversations();
        Map<String, EMConversation> allConversations = emChatManager.getAllConversations();
        List<Chat> result = new ArrayList<>();
        for (EMConversation emConversation : allConversations.values()) {
            result.add(IMUtils.emConversation2Chat(emConversation));
        }
        return result;
    }

    /**
     * 从网络上获取用户的群组列表（包括群成员）
     *
     * @return
     */
    private List<Group> getGroupListFromNet() {
        try {
            List<EMGroup> emGroupList = emGroupManager.getJoinedGroupsFromServer();
            List<Group> groupList = IMUtils.emGroupList2GroupList(emGroupList);
            for (Group group : groupList) {
                group.getMembers().addAll(getGroupMemberFromNet(group.getGroupId()));
            }
            return groupList;
        } catch (HyphenateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从网络上获取群组成员列表
     *
     * @param groupId
     * @return
     */
    private List<User> getGroupMemberFromNet(String groupId) {
        List<User> memberList = new ArrayList<>();
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
            memberList.addAll(IMUtils.usernameList2UserList(result.getData()));
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

        return memberList;
    }
}
