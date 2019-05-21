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

    public static Chat emConversation2Chat(EMConversation emConversation) {
        List<EMMessage> emMessageList = emConversation.getAllMessages();
        List<Message> messageList = new ArrayList<>();
        Chat.ChatType type = emConversation.getType() == EMConversation.EMConversationType.Chat ?
                Chat.ChatType.FRIEND : Chat.ChatType.GROUP;
        Chat chat = new Chat(type, messageList);
        for (EMMessage emMessage : emMessageList) {
            messageList.add(emMessage2Message(emMessage));
        }
        return chat;
    }

    public static Group emGroup2Group(EMGroup emGroup) {
        String groupId = emGroup.getGroupId();
        String groupName = emGroup.getGroupName();
        User owner = new User(emGroup.getOwner());
        return new Group(groupId, groupName, owner.getUsername(), new ArrayList<User>());
    }

    public static List<Group> emGroupList2GroupList(List<EMGroup> emGroupList) {
        List<Group> groupList = new ArrayList<>();
        for (EMGroup emGroup : emGroupList) {
            groupList.add(emGroup2Group(emGroup));
        }
        return groupList;
    }

    public static Message emMessage2Message(EMMessage emMessage) {
        return new Message(emMessage.getBody().toString(),
                emMessage.getFrom(),
                emMessage.getMsgTime(),
                emMessage.getTo());
    }

    public static List<User> usernameList2UserList(List<String> userNameList) {
        List<User> userList = new ArrayList<>();
        for (String userName : userNameList) {
            userList.add(new User(userName));
        }
        return userList;
    }
}
