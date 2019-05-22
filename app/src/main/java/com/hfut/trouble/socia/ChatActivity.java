package com.hfut.trouble.socia;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hfut.base.activity.BaseActivity;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.imlibrary.model.Message;
import com.hfut.trouble.R;

import butterknife.BindView;

public class ChatActivity extends BaseActivity {
    @BindView(R.id.lv_chat_message)
    ListView lvChatMessage;
    @BindView(R.id.et_message)
    EditText etMessage;
    @BindView(R.id.bt_send)
    Button btSend;

    MessageAdapter messageAdapter;
    Message.MessageType type;
    String targetId;
    String currentUserId;

    @Override
    public int getLayout() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        type = (Message.MessageType) intent.getSerializableExtra("type");
        targetId = intent.getStringExtra("target");
        currentUserId = IMManager.getInstance().getCurrentLoginUser().getUserId();

        //初始化消息列表
        messageAdapter = new MessageAdapter(getLayoutInflater(), currentUserId);
        lvChatMessage.setAdapter(messageAdapter);
        messageAdapter.setData(IMManager.getInstance().getMessageList(targetId));
        lvChatMessage.setSelection(messageAdapter.getCount() - 1);

        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s)){
                    btSend.setEnabled(false);
                }else{
                    btSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //点击按钮则发送消息
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMManager.getInstance().sendFriendMessage(etMessage.getText().toString(), targetId, new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                etMessage.setText("");
                                messageAdapter.setData(IMManager.getInstance().getMessageList(targetId));
                                lvChatMessage.setSelection(messageAdapter.getCount() - 1);
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(ChatActivity.this, "发送失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
