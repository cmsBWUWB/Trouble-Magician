package com.hfut.trouble.socia;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.hfut.base.activity.BaseActivity;
import com.hfut.imlibrary.model.Chat;

public class ChatActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Chat.ChatType type = (Chat.ChatType) intent.getSerializableExtra("type");
        String targetId = intent.getStringExtra("target");
        Toast.makeText(this, type == Chat.ChatType.FRIEND ? "friend" : "group" + " " + targetId, Toast.LENGTH_SHORT).show();
    }
}
