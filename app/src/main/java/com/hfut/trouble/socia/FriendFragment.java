package com.hfut.trouble.socia;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.event.FriendChangeEvent;
import com.hfut.imlibrary.model.Chat;
import com.hfut.imlibrary.model.Message;
import com.hfut.imlibrary.model.User;
import com.hfut.trouble.R;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendFragment extends Fragment {
    @BindView(R.id.lv_friend)
    ListView lvFriendList;
    private FriendListAdapter friendListAdapter;

    public static FriendFragment newInstance() {

        Bundle args = new Bundle();

        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_socia_friend, container, false);
        ButterKnife.bind(this, v);

        friendListAdapter = new FriendListAdapter(inflater);
        lvFriendList.setAdapter(friendListAdapter);

        lvFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = friendListAdapter.getItem(position).getUserId();
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("type", Message.MessageType.FRIEND);
                intent.putExtra("target", userId);
                startActivity(intent);
            }
        });

        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                List<User> friendList = IMManager.getInstance().getFriendListFromNet();
                if(friendList == null){
                    return;
                }
                ThreadDispatcher.getInstance().postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        friendListAdapter.setData(IMManager.getInstance().getFriendListFromLocal());
                    }
                });
            }
        });

        EventBus.getDefault().register(this);
        return v;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onFriendChangeEvent(FriendChangeEvent event){
        friendListAdapter.setData(IMManager.getInstance().getFriendListFromLocal());
    }
}
