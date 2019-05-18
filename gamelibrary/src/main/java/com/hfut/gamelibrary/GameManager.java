package com.hfut.gamelibrary;

import android.text.TextUtils;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.imlibrary.model.Group;
import com.hfut.imlibrary.model.User;

import java.util.List;

/**
 * 游戏房间以一个特殊的群组存在，有特定且唯一的群名决定
 *
 * 游戏步骤如下：
 * 1. 创建房间--创建游戏群组
 * 2. 加入房间--加入游戏群组
 * 3. 开始游戏--在游戏群组里面发送一个开始消息，只有创建者才能开始游戏和终止游戏
 * 4. 终止游戏--在游戏群组里面发送一个终止消息
 * 5. 重新开始--终止游戏 + 开始游戏
 * 6. 解散房间--销毁游戏群组
 * 7. 退出房间--退出游戏群组
 *
 * @author cms
 *
 */
public class GameManager {
    public static final String GAME_GROUP_NAME = "unique trouble magician game";
    private IMManager imManager;
    private Group currentRoom;
    private Game currentGame;

    private static GameManager instance = new GameManager();
    private GameManager(){
        imManager = IMManager.getInstance();
    }
    public static GameManager getInstance(){
        return instance;
    }


    /**
     * 判断是否已有游戏房间
     * @return
     */
    public boolean isAlreadyInRoom(){
        List<Group> groupList = imManager.requestGroupList();
        for(Group group:groupList){
            if(TextUtils.equals(group.getGroupName(), GAME_GROUP_NAME)){
                currentRoom = group;
                List<User> members = imManager.requestGroupMember(currentRoom.getGroupId());
                currentRoom.getMembers().addAll(members);
                return true;
            }
        }
        return false;
    }

    /**
     * 创建房间
     * @return
     */
    public boolean createRoom(){
        imManager.createGroup(GAME_GROUP_NAME, 10);
        return isAlreadyInRoom();
    }

    /**
     * 加入房间
     * @param roomId
     * @return
     */
    public boolean joinRoom(String roomId){
        imManager.joinGroup(roomId);
        return isAlreadyInRoom();
    }

    /**
     * 判断是否是房间所有者
     * @return
     */
    public boolean isRoomOwner(){
        return currentRoom.getOwner().equals(imManager.getCurrentLoginUser());
    }

    /**
     * 解散房间
     * @return
     */
    public boolean destroyRoom(){
        return imManager.destroyGroup(currentRoom.getGroupId());
    }

    /**
     * 退出房间
     * @return
     */
    public boolean exitRoom(){
        return imManager.exitGroup(currentRoom.getGroupId());
    }

    /**
     * 初始化游戏
     */
    public void initGame(){
        currentGame = new Game();
        currentGame.init(currentRoom.getMembers());
    }

    public void startGame(OperateCallBack callBack){
        //TODO 只有房间创建者才能开始游戏。
    }
    public void stopGame(){
    }
    public void restartGame(){
    }
}
