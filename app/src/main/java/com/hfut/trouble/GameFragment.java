package com.hfut.trouble;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.imlibrary.model.Group;
import com.hfut.trouble.game.GameRoomActivity;
import com.hfut.utils.callbacks.DefaultCallback;
import com.socks.library.KLog;

import org.jetbrains.annotations.NotNull;

public class GameFragment extends Fragment {

    public static GameFragment newInstance() {
        return new GameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        view.findViewById(R.id.bt_create_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMManager.getInstance().createGroup("测试房间", new DefaultCallback<Group>() {
                    @Override
                    public void onSuccess(Group group) {
                        if (group != null) {
                            Intent intent = new Intent(getActivity(), GameRoomActivity.class);
                            intent.putExtra(GameRoomActivity.TAG_GROUP, group);
                            startActivity(intent);
                        } else {
                            KLog.e("wzt", "group is null");
                        }
                    }

                    @Override
                    public void onFail(int errorCode, @NotNull String errorMsg) {
                        KLog.e("errorCode = " + errorCode + "; errorMsg = " + errorMsg);
                    }
                });
            }
        });
        view.findViewById(R.id.bt_join_room).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getContext();
                if (context == null) {
                    return;
                }
                EditText editText = new EditText(context);
                new AlertDialog.Builder(context)
                        .setMessage(R.string.enter_group_hint)
                        .setView(editText)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String groupId = editText.getText().toString().trim();
                                IMManager.getInstance().requestJoinGroup(groupId,new BaseEMCallBack(){
                                    @Override
                                    public void onSuccess() {
                                        super.onSuccess();
                                        IMManager.getInstance().getGroupFromServer(groupId, new DefaultCallback<Group>() {
                                            @Override
                                            public void onSuccess(Group group) {
                                                Intent intent = new Intent(context, GameRoomActivity.class);
                                                intent.putExtra(GameRoomActivity.TAG_GROUP, group);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void onFail(int errorCode, @NotNull String errorMsg) {
                                                KLog.e("errorMsg = " + errorMsg);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(int code, String error) {
                                        super.onError(code, error);
                                        KLog.e("error = " + error);
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
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
