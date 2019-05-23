package com.hfut.gamelibrary;

import androidx.annotation.Nullable;

import com.hfut.imlibrary.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cms
 */
public class Game {
    //卡牌种类
    public enum Card {
        DRAGON, DARK, DREAM, CAT_BIRD, FLASH, SNOW, FIRE, BLOOD
    }

    //游戏的下一步是什么？
    public enum STEP {
        CURRENT_PLAYER_DO_MAGIC,//当前玩家施放魔法
        CURRENT_PLAYER_DO_MAGIC_OR_PASS,//请当前玩家选择：继续释放魔法还是结束自己的回合
        CURRENT_PLAYER_DO_THROW_DICE,//当前玩家掷骰子
        START_NEW_TURN,//开始新一轮游戏
        GAME_END//整局游戏结束
    }

    private STEP nextStep;

    public class Player {
        String userId;
        int point;//分数
        int blood;//血量
        List<Card> cardList;//手里的牌

        public void changeBlood(int count) {
            blood += count;
            blood = Math.min(6, blood);
            blood = Math.max(0, blood);
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof Player && ((Player) obj).userId.equals(this.userId);
        }
    }

    private List<Player> playerList;//所有玩家

    //当前游戏的状态
    private int[] count;//每组牌的剩余个数
    private int totalCount;//剩余牌的总牌数

    private int currentTurn;//当前是第几轮游戏？
    private Player currentPlayer;//现在游戏是谁的回合？？

    /**
     * 每个玩家的回合都对应着一个Session
     */
    public class Session {
        public Card whichMagicList;//当前玩家本回合内释放的魔法
        public boolean doMagicSuccess;//当前玩家本回合内释放的魔法是否成功
        public int dice;//当前玩家本回合内魔法掷骰子的点数：-1表示没有掷骰子
    }

    private Session currentSession;

    /**
     * 初始化游戏
     */
    public void init(List<User> userList) {
        playerList = new ArrayList<>();
        for (User user : userList) {
            Player player = new Player();
            player.blood = 6;
            player.point = 0;
            player.userId = user.getUserId();
            playerList.add(player);
        }
        currentTurn = 0;
    }

    /**
     * 获取当前玩家
     */
    public String getCurrentPlayer() {
        return currentPlayer.userId;
    }

    /**
     * 获取游戏的下一步是什么？
     * 1. 当前玩家施放魔法
     * 2. 当前玩家掷骰子
     * 3. 开始新一轮游戏
     * 4. 游戏结束
     */
    public STEP getNextStep() {
        return nextStep;
    }

    /**
     * 施放魔法
     */
    public void doMagic(Card magic) {
        currentSession = new Session();
        currentSession.whichMagicList = magic;

        //判断当前玩家是否有该魔法
        int index = currentPlayer.cardList.indexOf(magic);

        if (index == -1) {
            //释放魔法失败
            currentSession.doMagicSuccess = false;
            switch (magic) {
                case DRAGON:
                    //1. 释放的是巨龙魔法，需要掷骰子，决定自己扣多少血
                    nextStep = STEP.CURRENT_PLAYER_DO_THROW_DICE;
                    break;
                default:
                    //下一个玩家
                    nextPlayer();
                    nextStep = STEP.CURRENT_PLAYER_DO_MAGIC;
                    break;
            }
        } else {
            //释放魔法成功
            currentSession.doMagicSuccess = true;
            currentPlayer.cardList.remove(index);

            switch (magic) {
                case DRAGON:
                case DREAM:
                    //掷骰子
                    nextStep = STEP.CURRENT_PLAYER_DO_THROW_DICE;
                    break;
                case DARK:
                    //黑暗幽灵
                    for (Player player : playerList) {
                        if (!player.equals(currentPlayer))
                            player.changeBlood(-1);
                    }
                    nextStep = STEP.CURRENT_PLAYER_DO_MAGIC_OR_PASS;
                    break;
            }
        }
    }

    private void nextPlayer() {
        currentSession = null;

        int currentPlayerIndex = playerList.indexOf(currentPlayer);
        currentPlayerIndex++;
        currentPlayerIndex %= playerList.size();
        currentPlayer = playerList.get(currentPlayerIndex);
    }

    /**
     * 结束当前玩家的回合
     */
    public void pass() {
        nextStep = STEP.CURRENT_PLAYER_DO_MAGIC;
        nextPlayer();
    }

    /**
     * 掷骰子
     */
    public void doThrowDice() {
        currentSession.dice = getDice();

        switch (currentSession.whichMagicList) {
            case DRAGON:
                if (currentSession.doMagicSuccess) {
                    for (Player player : playerList) {
                        if (!player.equals(currentPlayer))
                            player.changeBlood(-1 * currentSession.dice);
                    }
                    nextStep = STEP.CURRENT_PLAYER_DO_MAGIC_OR_PASS;
                } else {
                    currentPlayer.changeBlood(-1 * currentSession.dice);

                    nextPlayer();
                    nextStep = STEP.CURRENT_PLAYER_DO_MAGIC;
                }
                break;
            case DREAM:
                currentPlayer.changeBlood(currentSession.dice);
                break;
        }
    }

    /**
     * 开始新一轮游戏
     */
    public void startNewTurn() {
        count = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        totalCount = 36;

        //两个人就移除12张牌，三个人就移除6张牌
        if (playerList.size() == 2) {
            //移除12个
            for (int i = 0; i < 12; i++)
                getCard();
        } else if (playerList.size() == 3) {
            //移除6个
            for (int i = 0; i < 6; i++)
                getCard();
        }
        //每个人发5张牌
        for (Player player : playerList) {
            player.cardList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                player.cardList.add(getCard());
            }
        }

        currentTurn++;
        currentPlayer = playerList.get(0);
        currentSession = null;
        nextStep = STEP.CURRENT_PLAYER_DO_MAGIC;
    }

    private int getDice() {
        return (int) (Math.random() * 3) + 1;
    }

    /**
     * 随机摸一张牌
     */
    private Card getCard() {
        int random = (int) (Math.random() * totalCount) + 1;
        for (int i = 0; i < count.length; i++) {
            if ((random -= count[i]) <= 0) {
                count[i]--;
                totalCount--;
                return Card.class.getEnumConstants()[i];
            }
        }
        //理论上永远不会执行到这一步
        return null;
    }
}
