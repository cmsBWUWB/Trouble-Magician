package com.hfut.imlibrary;

import com.hfut.imlibrary.model.Chat;
import com.hfut.imlibrary.model.Group;
import com.hfut.imlibrary.model.Message;
import com.hfut.imlibrary.model.User;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;

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
                emConversation.getLastMessage().getBody().toString());
    }

    public static Message emMessage2Message(EMMessage emMessage, String targetId) {
        Message.MessageType type = emMessage.getChatType() == EMMessage.ChatType.Chat ? Message.MessageType.FRIEND : Message.MessageType.GROUP;
        return new Message(
                emMessage.getFrom(),
                type,
                targetId,
                emMessage.getBody().toString(),
                emMessage.getMsgTime());
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
