package com.hfut.trouble;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import android.view.View;
import android.widget.EditText;

import com.hfut.base.fragment.BaseFragment;
import com.hfut.gamelibrary.GameManager;
import com.hfut.base.manager.IMManager;
import com.hfut.imlibrary.model.Group;
import com.hfut.trouble.game.GameRoomActivity;
import com.hfut.utils.callbacks.DefaultCallback;
import com.hfut.utils.utils.log.LogPrint;

import org.jetbrains.annotations.NotNull;

import butterknife.OnClick;

public class GameFragment extends BaseFragment {

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_game;
    }

    @OnClick(R.id.bt_create_room)
    void clickCreateRoom(View view) {
        GameManager.getInstance().createGameRoom("测试房间", new DefaultCallback<Group>() {
            @Override
            public void onSuccess(Group group) {
                if (group != null) {
                    Intent intent = new Intent(getActivity(), GameRoomActivity.class);
                    intent.putExtra(GameRoomActivity.TAG_GROUP, group);
                    startActivity(intent);
                } else {
                    LogPrint.e("group is null");
                }
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                LogPrint.e("errorCode = " + errorCode + "; errorMsg = " + errorMsg);
            }
        });
    }

    @OnClick(R.id.bt_join_room)
    void clickJoinRoom(View view) {
        Context context = getContext();
        if (context != null) {
            showJoinDialog(context);
        }
    }

    private void showJoinDialog(final Context context) {
        EditText editText = new EditText(context);
        new AlertDialog.Builder(context)
                .setMessage(R.string.enter_group_hint)
                .setView(editText)
                .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String groupId = editText.getText().toString().trim();
                        GameManager.getInstance().joinGameRoom(groupId, new DefaultCallback<Group>(){
                            @Override
                            public void onSuccess(Group value) {
                                goToGameRoomActivity(context, groupId);
                            }
                            @Override
                            public void onFail(int errorCode, @NotNull String errorMsg) {
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void goToGameRoomActivity(final Context context,final String groupId) {
        IMManager.getInstance().getGroupFromServer(groupId, new DefaultCallback<Group>() {
            @Override
            public void onSuccess(Group group) {
                Intent intent = new Intent(context, GameRoomActivity.class);
                intent.putExtra(GameRoomActivity.TAG_GROUP, group);
                startActivity(intent);
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                LogPrint.e("errorMsg = " + errorMsg);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
