package com.hfut.trouble;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfut.base.thread.BusinessRunnable;
import com.hfut.base.thread.ThreadDispatcher;
import com.hfut.imlibrary.IMUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.et_username)
    public EditText etUsername;
    @BindView(R.id.et_password)
    public EditText etPassword;
    @BindView(R.id.bt_register)
    public Button btRegister;
    @BindView(R.id.bt_login)
    public Button btLogin;

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
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                    @Override
                    public void doWorkInRun() {
                        IMUtils.getInstance().register(username, password);
                    }
                });
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                    @Override
                    public void doWorkInRun() {
                        IMUtils.getInstance().login(username, password);
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onRegisterEvent(IMUtils.RegisterEvent event) {
        if (event.isSuccess()) {
            Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "注册失败！", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onLoginEvent(IMUtils.LoginEvent event) {
        if (event.isSuccess()) {
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "登录失败！", Toast.LENGTH_SHORT).show();
        }
    }


}
