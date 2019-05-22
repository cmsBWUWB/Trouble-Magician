package com.hfut.trouble.game;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hfut.base.activity.BaseActivity;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.imlibrary.listener.BaseGroupChangeListener;
import com.hfut.imlibrary.model.Group;
import com.hfut.trouble.R;
import com.socks.library.KLog;

import butterknife.BindView;

/**
 * Created by wzt on 2019/5/22
 * 游戏房间Activity
 * 进入房间生成随机房间id号
 */
public class GameRoomActivity extends BaseActivity {
    public static String TAG_GROUP = "tag_group";

    @BindView(R.id.tv_group_id)
    TextView tvGroupId;

    private Group mGroup;
    private BaseGroupChangeListener mGroupChangeListener;

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
            initListener();
        }
    }

    private void initListener() {
        mGroupChangeListener = new BaseGroupChangeListener(mGroup.getGroupId()) {
            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                super.onRequestToJoinAccepted(groupId, groupName, accepter);
                KLog.i("wzt", "groupName = " + groupName);
            }

            @Override
            public void onInvitationAccepted(String groupId, String invitee, String reason) {
                super.onInvitationAccepted(groupId, invitee, reason);
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
