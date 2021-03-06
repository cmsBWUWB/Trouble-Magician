package com.hfut.gamelibrary;

import android.util.Base64;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 游戏逻辑处理类
 *
 * @author cms
 */
public class Game implements Serializable {
    //卡牌种类
    public enum Card {
        DRAGON, DARK, DREAM, CAT_BIRD, FLASH, SNOW, FIRE, BLOOD;

        public static Card indexOf(int index) {
            switch (index){
                case 0:
                    return DRAGON;
                case 1:
                    return DARK;
                case 2:
                    return DREAM;
                case 3:
                    return CAT_BIRD;
                case 4:
                    return FLASH;
                case 5:
                    return SNOW;
                case 6:
                    return FIRE;
                case 7:
                    return BLOOD;
            }
            return null;
        }

        public int getIndex() {
            return this.ordinal();
        }
    }

    //游戏的状态
    public enum STATUS {
        WAIT_FOR_POST_CARD, ROUND_STARTED, ROUND_CONTINUE, WAIT_FOR_DICE, ROUND_ENDED, TURN_ENDED, GAME_ENDED
    }

    public static class Player implements Serializable {
        private String userId;
        private int point;//分数
        private int blood;//血量
        private ArrayList<Card> cardList;//手里的牌
        private ArrayList<Card> secretCardList;//猫头鹰的牌

        private void changeBlood(int count) {
            blood += count;
            blood = Math.min(6, blood);
            blood = Math.max(0, blood);
        }

        public String getUserId() {
            return userId;
        }

        public int getPoint() {
            return point;
        }

        public int getBlood() {
            return blood;
        }

        public ArrayList<Card> getCardList() {
            return cardList;
        }

        public ArrayList<Card> getSecretCardList() {
            return secretCardList;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            return obj instanceof Player && ((Player) obj).userId.equals(this.userId);
        }
    }

    //整局游戏相关的信息
    private ArrayList<Player> playerList;//所有玩家
    private Player gameWinner;//整局游戏的赢家是谁？
    private STATUS status;


    //每轮游戏相关的信息
    private Player turnWinner;//当前轮次的赢家是谁？
    private int[] useCount;//每种牌用了多少张，这里是指所有人都能看到的
    private ArrayList<Card> cardList;//剩余可抽取的牌
    private ArrayList<Card> secretCardList;//神秘牌组
    private ArrayList<Player> loser;//当前轮次哪些人死了？

    //每个回合相关的信息
    private Player currentPlayer;//现在游戏是谁的回合？？

    //每个回合的每个动作相关的信息
    private Card whichMagic;
    private boolean doMagicSuccess;
    private int dice;

    void init(ArrayList<String> userIdList){
        if(userIdList.size() < 2){
            throw new IllegalArgumentException();
        }
        playerList = new ArrayList<>();
        for (String userId : userIdList) {
            Player player = new Player();
            player.userId = userId;
            player.point = 0;
            player.blood = 6;
            player.cardList = new ArrayList<>();
            player.secretCardList = new ArrayList<>();
            playerList.add(player);
        }
        gameWinner = null;

        turnWinner = null;
        loser = new ArrayList<>();
        cardList = new ArrayList<>();
        secretCardList = new ArrayList<>();

        currentPlayer = playerList.get(0);

        whichMagic = null;
        doMagicSuccess = false;
        dice = 0;

        status = STATUS.WAIT_FOR_POST_CARD;
    }

    ArrayList<Card> generateCard(){
        int[] count = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        ArrayList<Card> cardList = new ArrayList<>();
        int totalCount = 36;
        for (int i = 0; i < 36; i++) {
            Card card = randomCard(count, totalCount);
            totalCount--;
            cardList.add(card);
        }
        return cardList;
    }

    /**
     * 随机选一张牌
     */
    private Card randomCard(int[] count, int totalCount) {
        int random = (int) (Math.random() * totalCount) + 1;
        for (int i = 0; i < count.length; i++) {
            if ((random -= count[i]) <= 0) {
                count[i]--;
                return Card.indexOf(i);
            }
        }
        //理论上永远不会执行到这一步
        return null;
    }

    void postCard(ArrayList<Card> cardList){

        //洗牌
        this.useCount = new int[8];
        this.cardList.clear();
        this.secretCardList.clear();

        //发牌
        this.cardList.addAll(cardList);

        //选择4张牌到猫头鹰那里
        secretCardList.add(getCard());
        secretCardList.add(getCard());
        secretCardList.add(getCard());
        secretCardList.add(getCard());

        //两个人就移除12张牌，三个人就移除6张牌
        if (playerList.size() == 2) {
            //移除12个
            for (int i = 0; i < 12; i++) {
                Card useCard = getCard();
                this.useCount[useCard.getIndex()]++;
            }
        } else if (playerList.size() == 3) {
            //移除6个
            for (int i = 0; i < 6; i++) {
                Card useCard = getCard();
                this.useCount[useCard.getIndex()]++;
            }
        }

        //每个人发5张牌
        for (Player player : playerList) {
            player.cardList.clear();
            for (int i = 0; i < 5; i++) {
                player.cardList.add(getCard());
            }
            player.secretCardList.clear();
        }
        status = STATUS.ROUND_STARTED;
    }

    /**
     * 下一轮游戏
     */
    void nextTurn() {
        for (Player player : playerList) {
            player.blood = 6;
        }
        turnWinner = null;
        loser.clear();

        currentPlayer = playerList.get(0);

        whichMagic = null;
        doMagicSuccess = false;
        dice = 0;
        status = STATUS.WAIT_FOR_POST_CARD;
    }

    /**
     * 下一回合
     */
    void nextRound() {
        if (doMagicSuccess) {
            whichMagic = null;
            doMagicSuccess = false;
            dice = 0;
            status = STATUS.ROUND_CONTINUE;
        } else {
            nextPlayer();
        }
    }

    private void nextPlayer() {
        //补满5张牌除非没牌可以补
        while (currentPlayer.cardList.size() < 5 && cardList.size() > 0) {
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
     * 施放魔法
     */
    void doMagic(Card magic) {
        whichMagic = magic;
        //判断当前玩家是否有该魔法
        int index = currentPlayer.cardList.indexOf(magic);
        if (index == -1) {
            //释放魔法失败
            doMagicSuccess = false;
            switch (magic) {
                case DRAGON:
                    //释放的是巨龙魔法，需要掷骰子，决定自己扣多少血
                    status = STATUS.WAIT_FOR_DICE;
                    return;
                default:
                    currentPlayer.changeBlood(-1);
                    if (isTurnEndThenEndTurn()) {
                        return;
                    }
                    status = STATUS.ROUND_ENDED;
                    return;
            }
        } else {
            //释放魔法成功
            doMagicSuccess = true;
            Card useCard = currentPlayer.cardList.remove(index);
            this.useCount[useCard.getIndex()]++;

            switch (magic) {
                case DRAGON://古代巨龙
                case DREAM://甜蜜的梦
                    //掷骰子
                    status = STATUS.WAIT_FOR_DICE;
                    break;
                case DARK:
                    //黑暗幽灵
                    for (Player player : playerList) {
                        if (!player.equals(currentPlayer))
                            player.changeBlood(-1);
                    }
                    currentPlayer.changeBlood(1);
                    status = STATUS.ROUND_ENDED;
                    break;
                case CAT_BIRD:
                    //猫头鹰
                    if (secretCardList.size() > 0) {
                        currentPlayer.secretCardList.add(secretCardList.remove(secretCardList.size() - 1));
                    }
                    status = STATUS.ROUND_ENDED;
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
                    status = STATUS.ROUND_ENDED;
                    break;
                case SNOW:
                    //暴风雪
                    //左边的玩家扣血
                    playerList.get((playerList.indexOf(currentPlayer) - 1 + playerList.size()) % playerList.size()).changeBlood(-1);
                    status = STATUS.ROUND_ENDED;
                    break;
                case FIRE:
                    //火球术
                    //右边的玩家扣血
                    playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size()).changeBlood(-1);
                    status = STATUS.ROUND_ENDED;
                    break;
                case BLOOD:
                    //魔法药水，自己加血
                    currentPlayer.changeBlood(1);
                    status = STATUS.ROUND_ENDED;
                    break;
            }
            if(isTurnEndThenEndTurn()){
                isGameEndThenEndGame();
                return;
            }
            return;
        }
    }

    /**
     * 掷骰子
     */
    void doThrowDice(int dice) {
        this.dice = dice;

        switch (whichMagic) {
            case DRAGON:
                if (doMagicSuccess) {
                    for (Player player : playerList) {
                        if (!player.equals(currentPlayer))
                            player.changeBlood(-1 * this.dice);
                    }
                    if(isTurnEndThenEndTurn()){
                        isGameEndThenEndGame();
                        return;
                    }
                    status = STATUS.ROUND_ENDED;
                } else {
                    currentPlayer.changeBlood(-1 * this.dice);
                    if(isTurnEndThenEndTurn()){
                        isGameEndThenEndGame();
                        return;
                    }
                    status = STATUS.ROUND_ENDED;
                }
                break;
            case DREAM:
                currentPlayer.changeBlood(this.dice);
                status = STATUS.ROUND_ENDED;
                break;
        }
    }

    int getDice() {
        return (int) (Math.random() * 3) + 1;
    }

    /**
     * 玩家主动结束自己的回合
     */
    void pass() {
        nextPlayer();
    }

    public Player getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * 获取所有玩家
     */
    public List<Player> getPlayerList() {
        return playerList;
    }

    /**
     * 用了多少张牌？这里指所有人都能看到的
     */
    public int[] getUseCount() {
        return useCount;
    }

    /**
     * 神秘牌组还剩多少张牌？
     */
    public int getSecretCardCount() {
        return secretCardList.size();
    }

    public Player getGameWinner() {
        return gameWinner;
    }

    public Player getTurnWinner() {
        return turnWinner;
    }

    public STATUS getStatus() {
        return status;
    }

    /**
     * 摸一张牌
     */
    private Card getCard() {
        return this.cardList.remove(this.cardList.size() - 1);
    }

    private boolean isTurnEndThenEndTurn() {
        //有赢家，其他所有人都是输家
        if(currentPlayer.cardList.isEmpty()){
            status = STATUS.TURN_ENDED;
            //玩家将手上的牌全部用完
            turnWinner = currentPlayer;
            turnWinner.point += 3 + turnWinner.secretCardList.size();
            return true;
        }

        //只有输家，没有赢家
        if(currentPlayer.blood == 0){
            status = STATUS.TURN_ENDED;
            for (Player player : playerList) {
                if (!player.equals(currentPlayer)) {
                    //除了输家，其他人都加一分
                    player.point += 1 + player.secretCardList.size();
                }
            }
            return true;
        }

        //某人把另外一个人打死了
        loser = new ArrayList<>();
        for (Player player : playerList) {
            if (player.blood == 0) {
                loser.add(player);
            }
        }
        if (!loser.isEmpty()) {
            status = STATUS.TURN_ENDED;
            turnWinner = currentPlayer;
            for (Player player : playerList) {
                if (player.equals(turnWinner)) {
                    //赢家加三分
                    player.point += 3 + player.secretCardList.size();
                } else if (loser.indexOf(player) == -1) {
                    //其他存活的玩家加一分
                    player.point += 1 + player.secretCardList.size();
                }
            }
            return true;
        }
        return false;
    }

    private boolean isGameEndThenEndGame() {
        for (Player player : playerList) {
            if (player.point >= 8) {
                status = STATUS.GAME_ENDED;
                gameWinner = player;
                return true;
            }
        }
        return false;
    }


    String toText() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        String string = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return string;
    }

    static Game toGame(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Game game = (Game) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return game;
    }


    static String fromCardList(ArrayList<Card> cardList) throws IOException{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream;
        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(cardList);
        String string = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return string;
    }

    static ArrayList<Card> toCardList(String str) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(Base64.decode(str, Base64.DEFAULT));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ArrayList<Card> cardArrayList = (ArrayList<Card>) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return cardArrayList;
    }
}
