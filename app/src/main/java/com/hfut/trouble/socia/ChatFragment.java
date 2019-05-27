package com.hfut.trouble.socia;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hfut.base.fragment.BaseFragment;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.event.MessageReceivedEvent;
import com.hfut.trouble.R;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

import static com.hfut.trouble.socia.ChatActivity.KEY_TARGET;
import static com.hfut.trouble.socia.ChatActivity.KEY_TYPE;

public class ChatFragment extends BaseFragment {
    @BindView(R.id.lv_chat)
    ListView lvChatList;

    private ChatListAdapter chatListAdapter;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_socia_chat;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        chatListAdapter = new ChatListAdapter(inflater);
        lvChatList.setAdapter(chatListAdapter);
        chatListAdapter.setData(IMManager.getInstance().getConversationListFromLocal());
        lvChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(KEY_TYPE, chatListAdapter.getItem(position).getMessageType());
                intent.putExtra(KEY_TARGET, chatListAdapter.getItem(position).getTargetId());
                startActivity(intent);
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
    public void onMessageReceivedEvent(MessageReceivedEvent event){
        Log.e("ssss", "onMessageReceivedEvent: ");
        chatListAdapter.setData(IMManager.getInstance().getConversationListFromLocal());
    }
}
