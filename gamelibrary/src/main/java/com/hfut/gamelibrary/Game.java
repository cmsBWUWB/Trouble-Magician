package com.hfut.gamelibrary;

import androidx.annotation.Nullable;

import com.hfut.imlibrary.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 游戏的规则不应该定义在这里，因为如果连游戏的规则都不知道，更别说使用Game类了
 *
 * @author cms
 */
public class Game {
    //卡牌种类
    public enum Card {
        DRAGON, DARK, DREAM, CAT_BIRD, FLASH, SNOW, FIRE, BLOOD;
        public static Card indexOf(int index){
            return Card.class.getEnumConstants()[index];
        }
    }
    //游戏的状态
    public enum STATUS {ROUND_STARTED, ROUND_ENDED, TURN_ENDED, GAME_ENDED}

    public class Player {
        private String userId;
        private int point;//分数
        private int blood;//血量
        private List<Card> cardList;//手里的牌
        private List<Card> secretCard;//猫头鹰的牌

        private void changeBlood(int count) {
            blood += count;
            blood = Math.min(6, blood);
            blood = Math.max(0, blood);
        }

        public int getPoint() {
            return point;
        }

        public int getBlood() {
            return blood;
        }

        public List<Card> getCardList() {
            return cardList;
        }

        public List<Card> getSecretCard() {
            return secretCard;
        }

        public String getUserId() {
            return userId;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof Player && ((Player) obj).userId.equals(this.userId);
        }
    }

    //整局游戏相关的信息
    private List<Player> playerList;//所有玩家
    private Player gameWinner;//整局游戏的赢家是谁？
    private STATUS status;


    //每轮游戏相关的信息
    private int[] count;//每组牌的剩余个数
    private int totalCount;//剩余牌的总牌数
    private int secretCardCount;//剩余神秘牌组的数量
    private Player turnWinner;//当前轮次的赢家是谁？
    private List<Player> loser;//当前轮次哪些人死了？

    //每个回合相关的信息
    private Player currentPlayer;//现在游戏是谁的回合？？

    //每个动作相关的信息
    private Card whichMagic;
    private boolean doMagicSuccess;
    private int dice;

    /**
     * 开始游戏
     */
    public void start(List<User> userList) {
        playerList = new ArrayList<>();
        for (User user : userList) {
            Player player = new Player();
            player.blood = 6;
            player.point = 0;
            player.userId = user.getUserId();
            playerList.add(player);
        }
        gameWinner = null;

        startTurn();
    }

    /**
     * 开始第一轮游戏
     */
    private void startTurn() {
        for (Player player: playerList) {
            player.blood = 6;
        }
        turnWinner = null;
        loser = new ArrayList<>();

        postCard();

        startRound();
    }

    /**
     * 开始第一个回合
     */
    private void startRound(){
        currentPlayer = playerList.get(0);

        whichMagic = null;
        doMagicSuccess = false;
        dice = 0;

        status = STATUS.ROUND_STARTED;
    }

    /**
     * 下一回合
     */
    public void nextRound() {
        //补满5张牌
        while (currentPlayer.cardList.size() < 5 && totalCount > secretCardCount) {
            currentPlayer.cardList.add(getCard());
        }
        whichMagic = null;
        doMagicSuccess = false;
        dice = 0;
        int currentPlayerIndex = playerList.indexOf(currentPlayer);
        currentPlayerIndex++;
        currentPlayerIndex %= playerList.size();
        currentPlayer = playerList.get(currentPlayerIndex);
        status = STATUS.ROUND_STARTED;
    }

    /**
     * 下一轮游戏
     */
    public void nextTurn() {
        if (turnWinner != null) {
            if (!loser.isEmpty()) {
                //1. 有loser，有winner
                for (Player player : playerList) {
                    if (player.equals(turnWinner)) {
                        //赢家加三分
                        player.point += 3 + player.secretCard.size();
                    } else if (loser.indexOf(player) == -1) {
                        //其他存活的玩家加一分
                        player.point += 1 + player.secretCard.size();
                    }
                }
            } else {
                //2. 没loser，有winner
                //赢家加三分，其他人不加分
                turnWinner.point += 3 + turnWinner.secretCard.size();
            }

        } else {
            //3. 有loser，没winner
            for (Player player : playerList) {
                if (loser.indexOf(player) == -1) {
                    //除了输家，其他人都加一分
                    player.point += 1 + player.secretCard.size();
                }
            }
        }
        //判断是否有人达到了8分以上，如果达到，则游戏结束。
        for (Player player : playerList) {
            if (player.point >= 8) {
                gameWinner = player;
                endGame();
                return;
            }
        }

        for (Player player: playerList) {
            player.blood = 6;
        }
        turnWinner = null;
        loser = new ArrayList<>();
        postCard();
        startRound();
    }

    //整局游戏结束
    private void endGame() {
        status = STATUS.GAME_ENDED;
    }

    /**
     * 施放魔法
     */
    public boolean doMagic(Card magic) {
        whichMagic = magic;
        //判断当前玩家是否有该魔法
        int index = currentPlayer.cardList.indexOf(magic);
        if (index == -1) {
            //释放魔法失败
            doMagicSuccess = false;
            switch (magic) {
                case DRAGON:
                    //释放的是巨龙魔法，需要掷骰子，决定自己扣多少血
                    break;
                default:
                    currentPlayer.changeBlood(-1);
                    if (currentPlayer.blood == 0) {
                        loser.add(currentPlayer);
                        status = STATUS.TURN_ENDED;
                    }
                    //回合结束
                    status = STATUS.ROUND_ENDED;
                    break;
            }
            return false;
        } else {
            //释放魔法成功
            doMagicSuccess = true;
            currentPlayer.cardList.remove(index);

            if (currentPlayer.cardList.isEmpty()) {
                //本轮游戏结束
                turnWinner = currentPlayer;
                status = STATUS.TURN_ENDED;
                return true;
            }

            switch (magic) {
                case DRAGON://古代巨龙
                case DREAM://甜蜜的梦
                    //掷骰子
                    break;
                case DARK:
                    //黑暗幽灵
                    for (Player player : playerList) {
                        if (!player.equals(currentPlayer))
                            player.changeBlood(-1);
                    }
                    currentPlayer.changeBlood(1);
                    break;
                case CAT_BIRD:
                    //猫头鹰
                    if (secretCardCount >= 1) {
                        secretCardCount--;
                        currentPlayer.secretCard.add(getCard());
                    }
                    break;
                case FLASH:
                    //闪电暴风雨

                    //右边的玩家扣血
                    playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size()).changeBlood(-1);
                    //人数只有2人的时候，只会扣一次血
                    if (playerList.size() != 2) {
                        //左边的玩家扣血
                        playerList.get((playerList.indexOf(currentPlayer) - 1 + playerList.size()) % playerList.size()).changeBlood(-1);
                    }
                    break;
                case SNOW:
                    //暴风雪
                    //左边的玩家扣血
                    playerList.get((playerList.indexOf(currentPlayer) - 1 + playerList.size()) % playerList.size()).changeBlood(-1);
                    break;
                case FIRE:
                    //火球术
                    //右边的玩家扣血
                    playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size()).changeBlood(-1);
                    break;
                case BLOOD:
                    //魔法药水，自己加血
                    currentPlayer.changeBlood(1);
                    break;
            }
            //判断有谁血量为0
            for (Player player : playerList) {
                if (player.blood == 0) {
                    loser.add(player);
                }
            }
            if (!loser.isEmpty()) {
                turnWinner = currentPlayer;
                status = STATUS.TURN_ENDED;
            }
            return true;
        }
    }

    /**
     * 掷骰子
     */
    public int doThrowDice() {
        dice = (int) (Math.random() * 3) + 1;

        switch (whichMagic) {
            case DRAGON:
                if (doMagicSuccess) {
                    for (Player player : playerList) {
                        if (!player.equals(currentPlayer))
                            player.changeBlood(-1 * dice);
                    }
                } else {
                    currentPlayer.changeBlood(-1 * dice);
                    if (currentPlayer.blood == 0) {
                        loser.add(currentPlayer);
                        status = STATUS.TURN_ENDED;
                        return dice;
                    }
                    status = STATUS.ROUND_ENDED;
                }
                break;
            case DREAM:
                currentPlayer.changeBlood(dice);
                break;
        }
        return dice;
    }

    /**
     * 玩家主动结束自己的回合
     */
    public void pass() {
        status = STATUS.ROUND_ENDED;
    }

    /**
     * 获取所有玩家
     */
    public List<Player> getPlayerList(){
        return playerList;
    }

    /**
     * 还剩什么牌？
     */
    public int[] getCount(){
        return count;
    }

    /**
     * 神秘牌组还剩多少张牌？
     */
    public int getSecretCardCount(){
        return secretCardCount;
    }
    public Player getGameWinner(){
        return gameWinner;
    }
    public Player getTurnWinner(){
        return turnWinner;
    }
    public STATUS getStatus(){
        return status;
    }

    /**
     * 发牌
     */
    private void postCard(){
        //发牌
        count = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        totalCount = 36;
        secretCardCount = 4;
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
            player.secretCard = new ArrayList<>();
        }
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
                return Card.indexOf(i);
            }
        }
        //理论上永远不会执行到这一步
        return null;
    }
}
