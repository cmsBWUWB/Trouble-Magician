package com.hfut.gamelibrary;

import java.util.List;
import java.util.Queue;

public class Game {
    enum Card {DRAGON, FIRE, WATER, COLD, FLASH, DARK}//卡牌种类
    class Player {
        String username;
        int point;//分数
        int blood;//血量
        Queue<Card> cardQueue;//手里的牌
    }

    Queue<Card> cardQueue;//剩余可选的卡牌
    private List<Player> playerList;//所有玩家
    private Player currentPlayer;//当前施放魔法的玩家


    /**
     * 初始化
     */
    public void init(List<String> userName){

    }
    public String getNextOne(){
        int nextIndex = (playerList.indexOf(currentPlayer) + 1) % playerList.size();
        return playerList.get(nextIndex).username;
    }

    /**
     * 当前玩家施放魔法
     * @param card
     */
    public void currentPlayerDoMagic(Card card){
    }

}
