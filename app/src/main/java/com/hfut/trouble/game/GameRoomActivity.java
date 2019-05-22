package com.hfut.trouble.game;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hfut.base.activity.BaseActivity;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.imlibrary.listener.BaseGroupChangeListener;
import com.hfut.imlibrary.model.Group;
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

    private Group mGroup;
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
        mGroup = (Group) getIntent().getSerializableExtra(TAG_GROUP);
        if (mGroup == null) {
            showToast(R.string.error_hint);
        } else {
            final String strGroupId = getString(R.string.id_group, mGroup.getGroupId());
            tvGroupId.setText(strGroupId);
            gameRoomMemberAdatper = new GameRoomMemberAdatper(this, users);
            lvRoomMember.setAdapter(gameRoomMemberAdatper);
            syncGroupMember();
            initListener();
        }
    }

    private void syncGroupMember() {
        IMManager.getInstance().getGroupMemberList(mGroup.getGroupId(), new DefaultCallback<List<User>>() {
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
        mGroupChangeListener = new BaseGroupChangeListener(mGroup.getGroupId()) {
            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                super.onRequestToJoinAccepted(groupId, groupName, accepter);
                syncGroupMember();
                KLog.i("wzt", "groupName = " + groupName);
            }

            @Override
            public void onInvitationAccepted(String groupId, String invitee, String reason) {
                super.onInvitationAccepted(groupId, invitee, reason);
                syncGroupMember();
                KLog.i("wzt", "groupId = " + groupId);
            }


        };
        IMManager.getInstance().addGroupChangeListener(mGroupChangeListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (TextUtils.equals(mGroup.getOwnerUserId(), IMManager.getInstance().getCurrentLoginUser().getUserId())) {
            IMManager.getInstance().destroyGroup(mGroup.getGroupId(),new BaseEMCallBack(){
                @Override
                public void onSuccess() {
                    super.onSuccess();
                    showToast(R.string.destroy_group_success);
                    KLog.i("destroy group success");
                }

                @Override
                public void onError(int code, String error) {
                    super.onError(code, error);
                    showToast(R.string.destroy_group_fail);
                    KLog.i("code = " + code + ";errorMessage = " + error);
                }
            });
        }else {
            IMManager.getInstance().exitGroup(mGroup.getGroupId(),new BaseEMCallBack(){
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
        }
        IMManager.getInstance().removeGroupChangeListener(mGroupChangeListener);
    }
}
