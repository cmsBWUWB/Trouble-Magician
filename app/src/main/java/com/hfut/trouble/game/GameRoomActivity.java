package com.hfut.trouble.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hfut.base.activity.BaseActivity;
import com.hfut.gamelibrary.GameManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.imlibrary.listener.BaseGroupChangeListener;
import com.hfut.imlibrary.model.User;
import com.hfut.trouble.R;
import com.hfut.utils.callbacks.DefaultCallback;
import com.socks.library.KLog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by wzt on 2019/5/22
 * 游戏房间Activity
 */
public class GameRoomActivity extends BaseActivity {
    public static String TAG_GROUP = "tag_group";

    @BindView(R.id.tv_group_id)
    TextView tvGroupId;
    @BindView(R.id.lv_room_members)
    ListView lvRoomMember;
    @BindView(R.id.bt_start_game)
    Button btStartGame;

    private List<User> users = new ArrayList<>();
    private BaseGroupChangeListener mGroupChangeListener;
    private GameRoomMemberAdatper gameRoomMemberAdatper;

    @Override
    public int getLayout() {
        return R.layout.activity_game_room;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String strRoomId = getString(R.string.id_group, GameManager.getInstance().getRoomId());
        tvGroupId.setText(strRoomId);
        gameRoomMemberAdatper = new GameRoomMemberAdatper(this, users);
        lvRoomMember.setAdapter(gameRoomMemberAdatper);
        syncGroupMember();
        initListener();
    }

    private void syncGroupMember() {
        GameManager.getInstance().getMemberList(new DefaultCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> value) {
                users.clear();
                users.addAll(value);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gameRoomMemberAdatper.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                showToast(errorMsg);
                KLog.i("code = " + errorCode + ";errorMessage = " + errorMsg);
            }
        });
    }

    private void initListener() {
        mGroupChangeListener = new BaseGroupChangeListener(GameManager.getInstance().getRoomId()) {
            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                super.onRequestToJoinAccepted(groupId, groupName, accepter);
                syncGroupMember();
                KLog.i( "groupName = " + groupName);
            }

            @Override
            public void onInvitationAccepted(String groupId, String invitee, String reason) {
                super.onInvitationAccepted(groupId, invitee, reason);
                syncGroupMember();
                KLog.i("groupId = " + groupId);
            }

            @Override
            public void onMemberJoined(String groupId, String member) {
                super.onMemberJoined(groupId, member);
                KLog.i("groupId = " + groupId + ";member = " + member);
                syncGroupMember();
            }

            @Override
            public void onMemberExited(String groupId, String member) {
                super.onMemberExited(groupId, member);
                KLog.i("groupId = " + groupId + ";member = " + member);
                syncGroupMember();
            }
        };
        GameManager.getInstance().addGroupChangeListener(mGroupChangeListener);
        btStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameRoomActivity.this, GameActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GameManager.getInstance().exitGameRoom(new BaseEMCallBack(){
            @Override
            public void onSuccess() {
                super.onSuccess();
                showToast(R.string.exit_group_success);
                KLog.i("exit group success");
            }

            @Override
            public void onError(int code, String error) {
                super.onError(code, error);
                showToast(R.string.exit_group_fail);
                KLog.i("code = " + code + ";errorMessage = " + error);
            }
        });
        GameManager.getInstance().removeGroupChangeListener(mGroupChangeListener);
    }
}
