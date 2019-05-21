package com.hfut.trouble.socia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hfut.trouble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFragment extends Fragment{
    @BindView(R.id.lv_chat)
    ListView lvChatList;
    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_socia_chat, container, false);
        ButterKnife.bind(this, v);
        lvChatList.setAdapter(new ChatListAdapter(inflater));
        return v;
    }
}
