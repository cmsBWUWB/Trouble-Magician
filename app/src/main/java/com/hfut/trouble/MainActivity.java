package com.hfut.trouble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 业务规则如下：
 * 1. 每个人最多同时只能加入一个游戏，要想开始新的游戏，必须退出当前游戏。
 * 2. 每个人最开始可以为自己设置一个昵称，且不用关心密码等操作。启动则自动登录。
 * 3. 不可修改昵称（因为昵称需要自己建立服务器）
 */

public class MainActivity extends AppCompatActivity {
    @BindViews({R.id.bt_create_room, R.id.bt_dismiss_room})
    public Button btCreate;
    public Button btDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);



        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setListener() {

    }


}
