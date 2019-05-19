package com.hfut.imlibrary.listener;



import com.hfut.imlibrary.model.Message;

import java.util.List;

/**
 * 监听各种事件：如好友变化，群组变化等等
 */
public interface IMListener {
    void messageReceived(List<Message> messageList);
    void friendChanged();
    void groupChanged();
    void groupMemberChanged();


    //以下功能暂时不做

    /**
     * 有人申请添加好友
     * @param username 谁添加的？
     * @param reason 添加理由是什么？
     */
//    void addFriendRequest(String username, String reason);

    /**
     * 好友申请是否得到同意？
     * @param username 添加哪个好友的申请？
     * @param agree 是否同意？
     */
//    void addFriendAgree(String username, boolean agree);

    /**
     * 有人申请加入群组
     * @param username 谁添加的？
     * @param reason 添加理由是什么？
     */
//    void joinGroupRequest(String username, String reason);

    /**
     * 入群申请是否得到同意？
     * @param groupId 加入哪个群组的申请？
     * @param agree 是否同意？
     */
//    void joinGroupAgree(String groupId, boolean agree);

}
