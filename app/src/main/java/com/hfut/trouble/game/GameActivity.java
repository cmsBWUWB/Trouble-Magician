package com.hfut.trouble.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.hfut.base.activity.BaseActivity;
import com.hfut.gamelibrary.Game;
import com.hfut.gamelibrary.GameManager;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.model.Group;
import com.hfut.trouble.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class GameActivity extends BaseActivity implements GameManager.ShowMe {
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


    public static final String KEY_GROUP = "group";
    public static final String KEY_USER_LIST = "userList";

    private String currentUserId;

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

        Intent intent = getIntent();
        Group group = (Group) intent.getSerializableExtra(KEY_GROUP);
        String[] userIdList = intent.getStringArrayExtra(KEY_USER_LIST);
        GameManager.getInstance().init(group, userIdList, this);

        setListener();
    }

    /**
     * 测试
     */
    private void test(){
        Game.Player player = new Game.Player();
        player.setUserId("test");
        player.setBlood(3);
        player.setPoint(4);
        ArrayList<Game.Card> cardList = new ArrayList<>();
        cardList.add(Game.Card.FIRE);
        cardList.add(Game.Card.DRAGON);
        cardList.add(Game.Card.FIRE);
        cardList.add(Game.Card.SNOW);
        player.setCardList(cardList);

        List<Game.Player> playerList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            playerList.add(player);
        }
        playerAdapter.setData(playerList);
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
                Game.Card card = Game.Card.indexOf(spCard.getSelectedItemPosition());
                GameManager.getInstance().doMagic(card);
            }
        });
        btThrowDice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dice = GameManager.getInstance().getDice();
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
        buttonEnableOrNot(game);
        if(game.getStatus() == Game.STATUS.GAME_INITED){
            return;
        }
        playerAdapter.setData(game.getPlayerList());

        switch (game.getStatus()) {
            case ROUND_ENDED:
                //目前自动下一回合
                GameManager.getInstance().nextRound();
                break;
            case TURN_ENDED:
                //目前自动下一轮游戏
                GameManager.getInstance().newTurn();
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
            case ROUND_ENDED:
            case TURN_ENDED:
            case GAME_ENDED:
            case GAME_INITED:
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
}
