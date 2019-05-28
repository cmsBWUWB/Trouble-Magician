package com.hfut.trouble;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfut.base.activity.BaseActivity;
import com.hfut.base.manager.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;
import com.hfut.utils.utils.ToastUtils;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_username)
    public EditText etUsername;
    @BindView(R.id.et_password)
    public EditText etPassword;
    @BindView(R.id.bt_login)
    public Button btLogin;
    @BindView(R.id.bt_register)
    public Button btRegister;


    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.bt_register:
                register();
                break;
        }
    }

    public void login() {
        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                IMManager.getInstance().login(username, password, new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show("登录成功");
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                show("登录失败");
                            }
                        });
                    }
                });
            }
        });
    }

    public void show(String text) {
        ToastUtils.Companion.show(LoginActivity.this, text, Toast.LENGTH_SHORT);
    }

    public void register() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return;
        }
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                boolean success = IMManager.getInstance().register(username, password);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            show("注册成功");
                        } else {
                            show("注册失败");
                        }
                    }
                });
            }
        });
    }
}
