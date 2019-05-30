package com.hfut.trouble;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hfut.base.activity.BaseActivity;
import com.hfut.base.manager.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;

import butterknife.BindView;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_userId)
    public EditText etUserId;
    @BindView(R.id.et_password)
    public EditText etPassword;
    @BindView(R.id.bt_login)
    public Button btLogin;


    @Override
    public int getLayout() {
        return R.layout.activity_login;
    }

    public void onclick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void login() {
        final String userId = etUserId.getText().toString();
        final String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(password)) {
            return;
        }
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                IMManager.getInstance().login(userId, password, new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                        showToast(R.string.login_success);
                    }

                    @Override
                    public void onFailure() {
                        showToast(R.string.login_fail);
                    }
                });
            }
        });
    }
}
