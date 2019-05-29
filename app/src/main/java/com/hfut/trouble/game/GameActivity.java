package com.hfut.trouble.game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.hfut.base.activity.BaseActivity;
import com.hfut.gamelibrary.Game;
import com.hfut.gamelibrary.GameManager;
import com.hfut.base.manager.IMManager;
import com.hfut.trouble.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

import butterknife.BindView;

public class GameActivity extends BaseActivity implements GameManager.EventListener {
    @BindView(R.id.lv_game_player)
    ListView lvPlayerList;
    @BindView(R.id.sp_card_list)
    Spinner spCard;
    @BindView(R.id.bt_do_magic)
    Button btDoMagic;
    @BindView(R.id.bt_throw_dice)
    Button btThrowDice;
    @BindView(R.id.bt_pass)
    Button btPass;

    PlayerAdapter playerAdapter;

    private String currentUserId;
    private Game game;

    @Override
    public int getLayout() {
        return R.layout.activity_game;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        playerAdapter = new PlayerAdapter(getLayoutInflater(), new ArrayList<>());
        lvPlayerList.setAdapter(playerAdapter);

        currentUserId = IMManager.getInstance().getCurrentLoginUser().getUserId();

        GameManager.getInstance().startGame( this);
        game = GameManager.getInstance().getGame();
        showGame(game);

        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameManager.getInstance().endGame();
    }

    private void setListener() {
        btDoMagic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.Card card = Game.Card.indexOf(spCard.getSelectedItemPosition());
                GameManager.getInstance().doMagic(card);
                showGame(game);
            }
        });
        btThrowDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dice = GameManager.getInstance().getDice();
                GameManager.getInstance().doThrowDice(dice);
                showGame(game);

            }
        });
        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameManager.getInstance().pass();
                showGame(game);
            }
        });
    }

    /**
     * todo 将游戏显示在界面当中
     */
    public void showGame(Game game) {
        buttonEnableOrNot(game);
        playerAdapter.setData(game.getPlayerList());
        switch (game.getStatus()) {
            case ROUND_ENDED:
                //目前自动下一回合
                GameManager.getInstance().nextRound();
                showGame(game);
                break;
            case TURN_ENDED:
                //目前自动下一轮游戏
                GameManager.getInstance().nextTurn();
                showGame(game);
                break;
        }
    }

    /**
     * 确定需要启用哪些按钮
     */
    private void buttonEnableOrNot(Game game) {
        switch (game.getStatus()) {
            case ROUND_STARTED:
                btDoMagic.setEnabled(true);
                btPass.setEnabled(false);
                btThrowDice.setEnabled(false);
                break;
            case ROUND_CONTINUE:
                btDoMagic.setEnabled(true);
                btPass.setEnabled(true);
                btThrowDice.setEnabled(false);
                break;
            case WAIT_FOR_DICE:
                btDoMagic.setEnabled(false);
                btPass.setEnabled(false);
                btThrowDice.setEnabled(true);
                break;
            case WAIT_FOR_POST_CARD:
            case ROUND_ENDED:
            case TURN_ENDED:
            case GAME_ENDED:
                btDoMagic.setEnabled(false);
                btPass.setEnabled(false);
                btThrowDice.setEnabled(false);
                break;
        }
        if (game.getCurrentPlayer() == null || !game.getCurrentPlayer().getUserId().equals(currentUserId)) {
            btDoMagic.setEnabled(false);
            btPass.setEnabled(false);
            btThrowDice.setEnabled(false);
        }
    }

    @Override
    public void postCard() {
        showGame(game);
    }

    @Override
    public void otherDoMagic(String userId, Game.Card magic) {
        showGame(game);
    }

    @Override
    public void otherDoThrowDice(String userId, int dice) {
        showGame(game);
    }

    @Override
    public void otherPass(String userId) {
        showGame(game);
    }
}
