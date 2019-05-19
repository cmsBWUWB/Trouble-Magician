package com.hfut.gamelibrary;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;

/**
 * todo
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
    private IMManager imManager;
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
        return true;
    }

    /**
     * 创建房间
     * @return
     */
    public boolean createRoom(){
        return false;
    }

    /**
     * 加入房间
     * @param roomId
     * @return
     */
    public boolean joinRoom(String roomId){
        return false;
    }

    /**
     * 判断是否是房间所有者
     * @return
     */
    public boolean isRoomOwner(){
        return false;
    }

    /**
     * 解散房间
     * @return
     */
    public boolean destroyRoom(){
        return false;
    }

    /**
     * 退出房间
     * @return
     */
    public boolean exitRoom(){
        return false;
    }

    /**
     * 初始化游戏
     */
    public void initGame(){
    }

    public void startGame(OperateCallBack callBack){
        //TODO 只有房间创建者才能开始游戏。
    }
    public void stopGame(){
    }
    public void restartGame(){
    }
}
