package com.hfut.gamelibrary;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.imlibrary.model.Group;
import com.hfut.imlibrary.model.Message;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

public class GameManager {
    private static GameManager instance = new GameManager();

    private GameManager() {
    }

    public static GameManager getInstance() {
        return instance;
    }

    public interface ShowMe {
        void showGame(Game game);
    }

    public static final String TYPE_DICE = "dice";
    public static final String TYPE_MAGIC = "magic";
    public static final String TYPE_PASS = "pass";


    private ShowMe showMe;
    private Game game;
    private Group group;

    public void init(Group group, String[] userIdList, ShowMe showMe) {
        this.group = group;
        this.showMe = showMe;
        game = new Game();
        game.init(userIdList);

        newTurn();

        EventBus.getDefault().register(this);
    }

    public void newTurn() {
        String currentUserId = IMManager.getInstance().getCurrentLoginUser().getUserId();
        if (currentUserId.equals(group.getOwnerUserId())) {
            //如果自己是房主，则要发牌，并发送给所有人
            game.newTurn();
            showMe.showGame(game);
            ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                @Override
                public void doWorkInRun() {
                    try {
                        IMManager.getInstance().sendGroupMessage(game.toText(), GameManager.this.group.getGroupId(), new OperateCallBack() {
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

    public void exit() {
        EventBus.getDefault().unregister(this);
        this.showMe = null;
        this.game = null;
    }

    public void doMagic(final Game.Card card) {
        game.doMagic(card);
        showMe.showGame(game);

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
        showMe.showGame(game);

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
        showMe.showGame(game);
    }

    public void pass() {
        game.pass();
        showMe.showGame(game);

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
                if ((game.getStatus() == Game.STATUS.GAME_INITED || game.getStatus() == Game.STATUS.TURN_ENDED) && message.getAuthorId().equals(group.getOwnerUserId())) {
                    try {
                        game = Game.toGame(message.getContent());
                        showMe.showGame(game);
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
                        game.doMagic(Game.Card.valueOf(keyValue[1]));
                        showMe.showGame(game);
                        break;
                    case TYPE_DICE:
                        //如果是他人掷骰子，更新界面
                        game.doThrowDice(Integer.valueOf(keyValue[1]));
                        showMe.showGame(game);
                        break;
                    case TYPE_PASS:
                        game.pass();
                        showMe.showGame(game);
                        break;
                }
            }
        }
    }

}
