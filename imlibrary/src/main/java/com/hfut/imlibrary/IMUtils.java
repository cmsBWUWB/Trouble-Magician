package com.hfut.imlibrary;

import com.hfut.imlibrary.model.Chat;
import com.hfut.imlibrary.model.Group;
import com.hfut.imlibrary.model.Message;
import com.hfut.imlibrary.model.User;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

public class IMUtils {
    public static List<User> userIdList2UserList(List<String> userIdList) {
        List<User> userList = new ArrayList<>();
        for (String userId : userIdList) {
            userList.add(new User(userId, ""));
        }
        return userList;
    }

    public static Chat emConversation2Chat(EMConversation emConversation, String targetName) {
        Message.MessageType type = emConversation.getType() == EMConversation.EMConversationType.Chat ?
                Message.MessageType.FRIEND : Message.MessageType.GROUP;
        return new Chat(type,
                emConversation.conversationId(),
                targetName,
                ((EMTextMessageBody)emConversation.getLastMessage().getBody()).getMessage());
    }

    public static Message emMessage2Message(EMMessage emMessage) {
        Message.MessageType type = emMessage.getChatType() == EMMessage.ChatType.Chat ? Message.MessageType.FRIEND : Message.MessageType.GROUP;
        return new Message(
                emMessage.getFrom(),
                type,
                emMessage.conversationId(),
                ((EMTextMessageBody)emMessage.getBody()).getMessage(),
                emMessage.getMsgTime());
    }
    public static List<Message> emMessageList2MessageList(List<EMMessage> emMessageList){
        List<Message> messageList = new ArrayList<>();
        for(EMMessage emMessage:emMessageList){
            messageList.add(emMessage2Message(emMessage));
        }
        return messageList;
    }

    public static Group emGroup2Group(EMGroup emGroup) {
        String groupId = emGroup.getGroupId();
        String groupName = emGroup.getGroupName();
        User owner = new User(emGroup.getOwner(), "");
        return new Group(groupId, groupName, owner.getUserId());
    }

    public static List<Group> emGroupList2GroupList(List<EMGroup> emGroupList) {
        List<Group> groupList = new ArrayList<>();
        for (EMGroup emGroup : emGroupList) {
            groupList.add(emGroup2Group(emGroup));
        }
        return groupList;
    }
}
