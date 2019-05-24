package com.hfut.trouble.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hfut.base.activity.BaseActivity;
import com.hfut.gamelibrary.Game;
import com.hfut.gamelibrary.GameManager;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.model.Group;
import com.hfut.trouble.R;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;

public class GameActivity extends BaseActivity implements GameManager.ShowMe {

    @BindView(R.id.bt_do_magic)
    Button btDoMagic;
    @BindView(R.id.bt_throw_dice)
    Button btThrowDice;
    @BindView(R.id.bt_pass)
    Button btPass;

    private Group group;
    private String[] userIdList;
    private boolean isOwner;
    private Game game;
    private String currentUserId;

    public static final String KEY_GROUP = "group";
    public static final String KEY_USER_LIST = "userList";

    @Override
    public int getLayout() {
        return R.layout.activity_game;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra(KEY_GROUP);
        userIdList = intent.getStringArrayExtra(KEY_USER_LIST);

        currentUserId = IMManager.getInstance().getCurrentLoginUser().getUserId();
        isOwner = group.getOwnerUserId().equals(currentUserId);

        GameManager.getInstance().init(group, userIdList, this);

        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameManager.getInstance().exit();
    }

    private void setListener() {
        btDoMagic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.Card card = Game.Card.DRAGON;
                GameManager.getInstance().doMagic(card);
            }
        });
        btThrowDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dice = game.getDice();
                GameManager.getInstance().doThrowDice(dice);

            }
        });
        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameManager.getInstance().pass();
            }
        });
    }

    /**
     * todo 将游戏显示在界面当中
     */
    public void showGame(Game game) {
        switch (game.getStatus()) {
            case ROUND_STARTED:
                break;
            case ROUND_CONTINUE:
                break;
            case WAIT_FOR_DICE:
                btDoMagic.setEnabled(false);
                btPass.setEnabled(false);
                btThrowDice.setEnabled(true);
                break;
            case ROUND_ENDED:
                game.nextRound();
                if (game.getStatus() == Game.STATUS.ROUND_CONTINUE) {
                    btDoMagic.setEnabled(true);
                    btPass.setEnabled(true);
                    btThrowDice.setEnabled(false);
                } else {
                    btDoMagic.setEnabled(false);
                    btPass.setEnabled(false);
                    btThrowDice.setEnabled(false);
                }
                break;
            case TURN_ENDED:
                //弹窗提示用户开始下一回合
                game.nextTurn();
                if (game.getCurrentPlayer().getUserId().equals(currentUserId)) {
                    btDoMagic.setEnabled(true);
                    btPass.setEnabled(false);
                    btThrowDice.setEnabled(false);
                } else {
                    btDoMagic.setEnabled(false);
                    btPass.setEnabled(false);
                    btThrowDice.setEnabled(false);
                }
                break;
            case GAME_ENDED:
                btDoMagic.setEnabled(false);
                btPass.setEnabled(false);
                btThrowDice.setEnabled(false);
                break;
        }
        if (!game.getCurrentPlayer().getUserId().equals(currentUserId)) {
            btDoMagic.setEnabled(false);
            btPass.setEnabled(false);
            btThrowDice.setEnabled(false);
        }
    }
}
