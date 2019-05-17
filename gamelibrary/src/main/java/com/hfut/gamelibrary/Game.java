package com.hfut.gamelibrary;

import com.hfut.imlibrary.model.User;

import java.util.List;
import java.util.Queue;

/**
 * @author cms
 */
public class Game {
    enum Card {DRAGON, FIRE, WATER, COLD, FLASH, DARK}//卡牌种类
    public class Player {
        User user;//用户
        int point;//分数
        int blood;//血量
        Queue<Card> cardQueue;//手里的牌

    }
    private List<Player> playerList;//所有玩家

    private Queue<Card> cardQueue;//剩余可选的卡牌
    private Player currentPlayer;//当前施放魔法的玩家

    /**
     * 初始化
     */
    public void init(List<User> user){
    }

    /**
     * 当前玩家施放魔法
     * @param card
     */
    public void currentPlayerDoMagic(Card card){
    }

}
