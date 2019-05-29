package com.hfut.gamelibrary;

import com.hfut.base.manager.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.imlibrary.listener.BaseGroupChangeListener;
import com.hfut.imlibrary.model.Group;
import com.hfut.imlibrary.model.Message;
import com.hfut.imlibrary.model.User;
import com.hfut.utils.callbacks.DefaultCallback;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;
import com.hyphenate.EMError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private static GameManager instance = new GameManager();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return instance;
    }

    public interface EventListener {
        void postCard();

        void otherDoMagic(String userId, Game.Card magic);

        void otherDoThrowDice(String userId, int dice);

        void otherPass(String userId);
    }


    public static final String TYPE_DICE = "dice";
    public static final String TYPE_MAGIC = "magic";
    public static final String TYPE_PASS = "pass";

    private static final String GAME_GROUP_NAME = "game room";


    private Group group;
    private boolean isOwner;
    private ArrayList<String> userIdList;
    private Game game;

    private EventListener eventListener;

    public void isAlreadyInRoom(final DefaultCallback<Boolean> defaultCallback){
        IMManager.getInstance().getGroupListFromServer(new DefaultCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> value) {
                for(Group group:value){
                    if(group.getGroupName().equals(GAME_GROUP_NAME)){
                        GameManager.this.group = group;
                        isOwner = group.getOwnerUserId().equals(IMManager.getInstance().getCurrentLoginUser().getUserId());
                        defaultCallback.onSuccess(true);
                    }
                }
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                defaultCallback.onFail(errorCode, errorMsg);
            }
        });
    }

    /**
     * 加入游戏房间
     */
    public void joinGameRoom(final String groupId, final DefaultCallback<Group> defaultCallback) {
        isOwner = false;

        IMManager.getInstance().requestJoinGroup(groupId, new BaseEMCallBack(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                IMManager.getInstance().getGroupFromServer(groupId, new DefaultCallback<Group>() {
                    @Override
                    public void onSuccess(Group value) {
                        GameManager.this.group = value;
                        defaultCallback.onSuccess(group);
                    }

                    @Override
                    public void onFail(int errorCode, @NotNull String errorMsg) {
                        defaultCallback.onFail(errorCode, errorMsg);
                    }
                });
            }

            @Override
            public void onError(int code, String error) {
                super.onError(code, error);
                if(code == EMError.GROUP_ALREADY_JOINED){
                    //已经加入群组
                    IMManager.getInstance().getGroupFromServer(groupId, new DefaultCallback<Group>() {
                        @Override
                        public void onSuccess(Group value) {
                            GameManager.this.group = value;
                            defaultCallback.onSuccess(group);
                        }

                        @Override
                        public void onFail(int errorCode, @NotNull String errorMsg) {
                            defaultCallback.onFail(errorCode, errorMsg);
                        }
                    });
                }else {
                    defaultCallback.onFail(code, error);
                }
            }
        });
    }

    /**
     * 创建游戏房间
     */
    public void createGameRoom(String gameRoomName, final DefaultCallback<Group> defaultCallback) {
        isOwner = true;

        IMManager.getInstance().createGroup(gameRoomName, new DefaultCallback<Group>() {
            @Override
            public void onSuccess(Group value) {
                GameManager.this.group = value;
                defaultCallback.onSuccess(value);
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                defaultCallback.onFail(errorCode, errorMsg);
            }
        });
    }

    public void addGroupChangeListener(BaseGroupChangeListener mGroupChangeListener){
        IMManager.getInstance().addGroupChangeListener(mGroupChangeListener);
    }

    public void removeGroupChangeListener(BaseGroupChangeListener mGroupChangeListener){
        IMManager.getInstance().removeGroupChangeListener(mGroupChangeListener);
    }

    public void exitGameRoom(BaseEMCallBack baseEMCallBack) {
        this.eventListener = null;
        game = null;

        if (isOwner) {
            IMManager.getInstance().destroyGroup(group.getGroupId(), baseEMCallBack);
        } else {
            IMManager.getInstance().exitGroup(group.getGroupId(), baseEMCallBack);
        }
    }

    public String getRoomId() {
        return group.getGroupId();
    }

    public boolean isOwner(){
        return isOwner;
    }

    public String getRoomMaster(){
        return group.getOwnerUserId();
    }
    public void getRoomMemberList(final DefaultCallback<List<User>> defaultCallback) {
        IMManager.getInstance().getGroupMemberListFromServer(GameManager.getInstance().getRoomId(), new DefaultCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> value) {
                GameManager.this.userIdList = new ArrayList<>();
                for(User user:value) {
                    GameManager.this.userIdList.add(user.getUserId());
                }
                defaultCallback.onSuccess(value);
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                defaultCallback.onFail(errorCode, errorMsg);
            }
        });
    }

    public Game getGame(){
        return game;
    }

    /**
     * 开始游戏
     */
    public void startGame(EventListener eventListener) {
        if(!userIdList.contains(GameManager.getInstance().getRoomMaster())){
            userIdList.add(GameManager.getInstance().getRoomMaster());
        }
        this.eventListener = eventListener;
        EventBus.getDefault().register(this);

        game = new Game();
        game.init(userIdList);
        if (isOwner) {
            final ArrayList<Game.Card> cardArrayList = game.generateCard();
            game.postCard(cardArrayList);
            ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                @Override
                public void doWorkInRun() {
                    try {
                        IMManager.getInstance().sendGroupMessage(Game.fromCardList(cardArrayList), GameManager.this.group.getGroupId(), new OperateCallBack() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure() {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void endGame(){
        EventBus.getDefault().unregister(this);
    }

    public void doMagic(final Game.Card card) {
        game.doMagic(card);
        //告诉所有人我的选择
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                IMManager.getInstance().sendGroupMessage(TYPE_MAGIC + ":" + card.toString(), group.getGroupId(), new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        });
    }

    public void doThrowDice(final int dice) {
        game.doThrowDice(dice);
        //告诉所有人我投的骰子
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                IMManager.getInstance().sendGroupMessage(TYPE_DICE + ":" + dice, group.getGroupId(), new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        });
    }

    public int getDice() {
        return game.getDice();
    }


    public void nextRound() {
        game.nextRound();
    }

    public void nextTurn() {
        game.nextTurn();
        if(isOwner){
            final ArrayList<Game.Card> cardArrayList = game.generateCard();
            game.postCard(cardArrayList);
            ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                @Override
                public void doWorkInRun() {
                    try {
                        IMManager.getInstance().sendGroupMessage(Game.fromCardList(cardArrayList), GameManager.this.group.getGroupId(), new OperateCallBack() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onFailure() {
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void pass() {
        game.pass();
        //告诉所有人我的选择
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                IMManager.getInstance().sendGroupMessage(TYPE_PASS + ":", group.getGroupId(), new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        List<Message> messageList = event.getNewMessageList();
        for (Message message : messageList) {
            if (message.getChatId().equals(group.getGroupId())) {
                //该群来消息了，做些什么？
                //如果是房主的游戏发牌，更新界面
                if (game.getStatus() == Game.STATUS.WAIT_FOR_POST_CARD && message.getAuthorId().equals(group.getOwnerUserId())) {
                    try {
                        game.postCard(Game.toCardList(message.getContent()));
                        eventListener.postCard();
                        continue;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                String content = message.getContent();
                String[] keyValue = content.split(":");
                switch (keyValue[0]) {
                    case TYPE_MAGIC:
                        //如果是他人施放魔法，更新界面
                        Game.Card card = Game.Card.valueOf(keyValue[1]);
                        game.doMagic(card);
                        eventListener.otherDoMagic(message.getAuthorId(), card);
                        break;
                    case TYPE_DICE:
                        //如果是他人掷骰子，更新界面
                        int dice = Integer.valueOf(keyValue[1]);
                        game.doThrowDice(dice);
                        eventListener.otherDoThrowDice(message.getAuthorId(), dice);
                        break;
                    case TYPE_PASS:
                        game.pass();
                        eventListener.otherPass(message.getAuthorId());
                        break;
                }
            }
        }
    }

}
