package com.hfut.imlibrary;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.imlibrary.listener.BaseGroupChangeListener;
import com.hfut.imlibrary.listener.FriendChangeListener;
import com.hfut.imlibrary.listener.GroupChangeListener;
import com.hfut.imlibrary.listener.MessageReceivedListener;
import com.hfut.imlibrary.model.*;
import com.hfut.utils.callbacks.DefaultCallback;
import com.hfut.utils.utils.Utils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    private static final int NUM_GAMEER_DEFAULT = 5;
    private EMClient emClient;
    private EMChatManager emChatManager;
    private EMGroupManager emGroupManager;
    private EMContactManager emContactManager;

    private MessageReceivedListener messageReceivedListener;
    private GroupChangeListener groupChangeListener;
    private FriendChangeListener friendChangeListener;


    private User currentLoginUser;


    private static IMManager instance = new IMManager();

    private IMManager() {
    }

    public static IMManager getInstance() {
        return instance;
    }

    /**
     * 使用前必须先init
     */
    public void init(Application context) {
        EMOptions options = new EMOptions();
        emClient = EMClient.getInstance();
        emClient.init(context, options);
        if (isLogin()) {
            onLoginSuccess();
        }
    }

    /**
     * 注册
     *
     * @param userName 环信做的限制：必须小写字母
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

    /**
     * 是否已登录
     */
    public boolean isLogin() {
        return emClient.isLoggedInBefore();
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

    private void onLoginSuccess() {
        emChatManager = emClient.chatManager();
        emGroupManager = emClient.groupManager();
        emContactManager = emClient.contactManager();
        currentLoginUser = new User(emClient.getCurrentUser(), "");
        setListener();
    }

    private void onLogout() {
        unsetListener();
    }

    private void setListener() {
        messageReceivedListener = new MessageReceivedListener();
        groupChangeListener = new GroupChangeListener();
        friendChangeListener = new FriendChangeListener();

        emChatManager.addMessageListener(messageReceivedListener);
        emGroupManager.addGroupChangeListener(groupChangeListener);
        emContactManager.setContactListener(friendChangeListener);
    }

    private void unsetListener() {
        emChatManager.removeMessageListener(messageReceivedListener);
        emGroupManager.removeGroupChangeListener(groupChangeListener);
        emContactManager.removeContactListener(friendChangeListener);
    }

    public User getCurrentLoginUser() {
        return currentLoginUser;
    }

    /**
     * 获取好友列表
     */
    public List<User> getFriendList() {
        try {
            return IMUtils.userIdList2UserList(emContactManager.getAllContactsFromServer());
        } catch (HyphenateException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 申请添加好友
     */
    public void requestAddFriend(String userId,BaseEMCallBack baseEMCallBack) {
        emContactManager.aysncAddContact(userId, "", baseEMCallBack);
    }

    /**
     * 发送好友消息
     *
     * @param content 发送消息的内容
     * @param userId  发送消息的好友id
     */
    public void sendFriendMessage(String content, final String userId, final OperateCallBack callBack) {
        final EMMessage message = EMMessage.createTxtSendMessage(content, userId);
        message.setChatType(EMMessage.ChatType.Chat);

        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                emChatManager.saveMessage(message);
                Log.e(TAG, "send message onSuccess: " + userId);
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
     * 从本地获取聊天会话列表
     */
    public List<Chat> getConversationList() {
        emChatManager.loadAllConversations();
        Map<String, EMConversation> allConversations = emChatManager.getAllConversations();

        List<Chat> result = new ArrayList<>();
        for (EMConversation emConversation : allConversations.values()) {
            result.add(IMUtils.emConversation2Chat(emConversation, "todo"));
        }
        return result;
    }

    /**
     * 从本地获取聊天记录
     *
     * @param targetId 群id或者好友id
     */
    public List<Message> getMessageList(String targetId) {
        emChatManager.loadAllConversations();
        List<Message> result = new ArrayList<>();
        EMConversation emConversation = emChatManager.getConversation(targetId);
        if (emConversation != null) {
            List<EMMessage> emMessageList = emConversation.getAllMessages();
            List<EMMessage> temp = null;
            do {
                String firstMessageId = emMessageList.get(0).getMsgId();
                temp = emConversation.loadMoreMsgFromDB(firstMessageId, 10);
                emMessageList.addAll(0, temp);
            } while (temp.size() == 10);

            for (EMMessage emMessage : emMessageList) {
                result.add(IMUtils.emMessage2Message(emMessage, targetId));
            }
        }
        return result;
    }

    /**
     * 创建群组
     */
    public void createGroup(String groupName, @NotNull DefaultCallback<Group> callback) {
        createGroup(groupName, NUM_GAMEER_DEFAULT,callback);
    }

    /**
     * 创建群组
     */
    public void createGroup(final String groupName, final int maxUsers, @NotNull final DefaultCallback<Group> callback) {
        EMGroupManager.EMGroupOptions options = new EMGroupManager.EMGroupOptions();
        options.maxUsers = maxUsers;
        //设置创建的群组所有人都可以无条件加入
        options.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
        emGroupManager.asyncCreateGroup(groupName, "", new String[]{}, "", options, new EMValueCallBack<EMGroup>() {
            @Override
            public void onSuccess(EMGroup emGroup) {
                callback.onSuccess(IMUtils.emGroup2Group(emGroup));
            }

            @Override
            public void onError(int error, String errorMsg) {
                callback.onFail(error,errorMsg);
            }
        });
    }


    /**
     * 解散群组，只有创建者才能解散群组
     */
    public void destroyGroup(final String groupId, final BaseEMCallBack baseEMCallBack) {
        emGroupManager.asyncDestroyGroup(groupId, baseEMCallBack);
    }

    /**
     * 申请加入群组
     */
    public void requestJoinGroup(String groupId,BaseEMCallBack baseEMCallBack) {
        emGroupManager.asyncJoinGroup(groupId, baseEMCallBack);
    }

    /**
     * 退出群组
     */
    public void exitGroup(final String groupId,BaseEMCallBack baseEMCallBack) {
        emGroupManager.asyncLeaveGroup(groupId, baseEMCallBack);
    }

    /**
     * 拉人入群
     */
    public void inviteJoinGroup(String userId, String groupId, BaseEMCallBack baseEMCallBack) {
        emGroupManager.asyncInviteUser(groupId, new String[]{userId}, "", baseEMCallBack);
    }

    /**
     * 拉多人入群
     */
    public void inviteJoinGroup(List<String> userIdList, String groupId,BaseEMCallBack baseEMCallBack) {
        if (Utils.isListEmpty(userIdList)) {
            baseEMCallBack.onError(-1,"userIdList is empty");
        }
        String[] usernameList = (String[]) userIdList.toArray();
        emGroupManager.asyncInviteUser(groupId, usernameList, "", baseEMCallBack);
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
     * 获取当前用户所有的群
     */
    public void getGroupList(final DefaultCallback<List<Group>> callback) {
        emGroupManager.asyncGetJoinedGroupsFromServer(new EMValueCallBack<List<EMGroup>>() {
            @Override
            public void onSuccess(List<EMGroup> value) {
                callback.onSuccess(IMUtils.emGroupList2GroupList(value));
            }

            @Override
            public void onError(int error, String errorMsg) {
                callback.onFail(error, errorMsg);
            }
        });
    }

    /**
     * 获取群组的所有成员
     */
    public List<User> getGroupMemberList(String groupId) {
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
            memberList.addAll(IMUtils.userIdList2UserList(result.getData()));
        } while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

        return memberList;
    }

    public void addGroupChangeListener(BaseGroupChangeListener baseGroupChangeListener) {
        if (groupChangeListener != null) {
            groupChangeListener.addListener(baseGroupChangeListener);
        }
    }

    public void removeGroupChangeListener(BaseGroupChangeListener baseGroupChangeListener) {
        if (emGroupManager != null) {
            groupChangeListener.removeListener(baseGroupChangeListener);
        }
    }
}
