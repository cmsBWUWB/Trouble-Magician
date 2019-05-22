package com.hfut.trouble.socia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hfut.imlibrary.model.Chat;
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

        return v;
    }
}
