package com.hfut.trouble.socia;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class ChatFragment extends Fragment{
    public static ChatFragment newInstance() {
        Bundle args = new Bundle();

        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
