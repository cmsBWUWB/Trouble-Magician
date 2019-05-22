package com.hfut.trouble.socia;

import android.os.Bundle;

import android.widget.ListView;

import com.hfut.base.fragment.BaseFragment;
import com.hfut.trouble.R;

import butterknife.BindView;

public class ChatFragment extends BaseFragment {
    @BindView(R.id.lv_chat)
    ListView lvChatList;

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
}
