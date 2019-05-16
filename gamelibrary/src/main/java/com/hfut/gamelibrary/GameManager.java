package com.hfut.gamelibrary;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.model.Group;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 游戏步骤如下：
 * 1. 创建房间--创建一个群组
 * 2. 加入房间--加入一个群组
 * 3. 开始游戏--在群组里面发送一个开始消息，只有创建者才能开始游戏和终止游戏
 * 4. 终止游戏--在群组里面发送一个终止消息
 * 5. 重新开始--终止游戏 + 开始游戏
 * 6. 解散房间--销毁群组
 * 7. 退出房间--退出群组
 *
 */
public class GameManager {
    private static GameManager instance = new GameManager();
    private GameManager(){
    }
    public static GameManager getInstance(){
        return instance;
    }

    private enum NextAction{NEXT_ONE, NEXT_TURN, END_GAME}
    private static final String START_GAME = "start game!!!!!!!!";

    private String currentUsername;
    private List<Group> groupList;

    private Group currentGroup;
    private Game currentGame;

    /**
     * 初始化
     * @param userName
     * @param androidId
     */
    public void initGameManager(String userName, String androidId, final OperateCallBack callBack){
        IMManager.getInstance().register(userName, androidId);
        IMManager.getInstance().login(userName, androidId, callBack);
        currentUsername = userName;
    }

    /**
     * 因为业务规则是只能加入一个群组，所以这里判断是否已经有一个群组
     * @return
     */
    public boolean isAlreadyInRoom(){
        groupList = IMManager.getInstance().getAllGroupAndMember();
        return groupList != null && groupList.size() > 0;
    }

    public boolean createRoom(String groupName){
        boolean result = IMManager.getInstance().createGroup(groupName);
        if(result){
            groupList = IMManager.getInstance().getAllGroupAndMember();
            if(groupList.size() == 0){
                result = false;
            }
            currentGroup = groupList.get(0);
        }
        return result;
    }
    public boolean joinRoom(String groupId){
        boolean result = IMManager.getInstance().joinGroup(groupId);
        if(result){
            groupList = IMManager.getInstance().getAllGroupAndMember();
            if(groupList.size() == 0){
                result = false;
            }
            currentGroup = groupList.get(0);
        }
        return result;
    }

    public boolean exitRoom(){
        if(currentGroup.getOwner().equals(currentUsername)){
            return IMManager.getInstance().destroyGroup(currentGroup.getGroupId());
        }else{
            return IMManager.getInstance().exitGroup(currentGroup.getGroupId());
        }
    }

    public void startGame(OperateCallBack callBack){
        //TODO 只有房间创建者才能开始游戏。
        currentGame = new Game();
        currentGame.init(currentGroup.getMembers());
    }
    public void stopGame(){
    }
    public void restartGame(){
    }


    public NextAction nextAction(){
        //如果有人的血量到达了0，则死去，开始下一轮
        return NextAction.NEXT_TURN;
    }
    public void next(){
        switch (nextAction()){
            case NEXT_ONE:
                break;
            case NEXT_TURN:
                break;
            //todo
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessageReceivedEvent(MessageReceivedEvent event){

    }
}
