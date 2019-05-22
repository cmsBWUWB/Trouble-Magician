package com.hfut.trouble;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hfut.imlibrary.IMManager;
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
        Button button = view.findViewById(R.id.bt_create_room);
        button.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
