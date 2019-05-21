package com.hfut.trouble.socia;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hfut.base.activity.BaseActivity;

public class ChatActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        super.onCreate(savedInstanceState);
    }
}
