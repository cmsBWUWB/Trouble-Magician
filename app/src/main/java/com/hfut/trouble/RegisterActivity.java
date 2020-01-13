package com.hfut.trouble;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hfut.base.activity.BaseActivity;
import com.hfut.base.application.CoreManager;
import com.hfut.base.dialog.CameraHandler;
import com.hfut.base.manager.IMManager;
import com.hfut.base.manager.UserManager;
import com.hfut.base.model.CameraImageBean;
import com.hfut.base.model.RequestCodes;
import com.hfut.imlibrary.model.User;
import com.hfut.utils.callbacks.DefaultCallback;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;
import com.hfut.utils.utils.FileUtils;
import com.hfut.utils.utils.log.LogPrint;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import xiaoma.com.bomb.BmobManager;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    public ImageView ivHead;
    @BindView(R.id.et_userId)
    public EditText etUserId;
    @BindView(R.id.et_username)
    public EditText etUsername;
    @BindView(R.id.et_password)
    public EditText etPassword;

    String mPicPath = null;


    @Override
    public int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Glide.with(RegisterActivity.this)
                .load(R.drawable.icon_default_head)
                .transform(new CircleCrop())
                .into(ivHead);
    }

    @OnClick(R.id.iv_head)
    public void clickHead() {
        new CameraHandler(this).beginChooseDialog();
    }

    @OnClick(R.id.bt_register)
    public void register() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String userId = etUserId.getText().toString();
        if (TextUtils.isEmpty(userId)) {
            showToast(R.string.input_error);
            etUserId.requestFocus();
            return;
        }else if (TextUtils.isEmpty(username)) {
            showToast(R.string.input_error);
            etUsername.requestFocus();
            return;
        }else if (TextUtils.isEmpty(password)) {
            showToast(R.string.input_error);
            etPassword.requestFocus();
            return;
        }
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                boolean success = IMManager.getInstance().register(userId, password);
                if (success) {
                    User user = new User(userId, username);
                    user.setPassword(password);
                    user.setPicPath(mPicPath);
                    UserManager.INSTANCE.saveCurrentUser(user);
                    showToast("注册成功");
                    finish();
                } else {
                    showToast("注册失败");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCodes.TAKE_PHOTO:
                final Uri uri = CameraImageBean.INSTANCE.getPath();
                if (uri == null) {
                    LogPrint.e("uri is null!");
                    return;
                }
                showAndSaveHeadIcon(uri);
                break;
            case RequestCodes.PICK_PHOTO:
                //这里的uri是content开头，需要解析成file开头的才可以正常使用
                final Uri pickUri = data.getData();
                if (pickUri == null) {
                    LogPrint.e("uri is null!");
                    return;
                }
                showAndSaveHeadIcon(pickUri);
                break;
            default:
                break;
        }
    }

    private void showAndSaveHeadIcon(Uri uri) {
        Glide.with(RegisterActivity.this)
                .load(uri)
                .transform(new CircleCrop())
                .error(R.drawable.icon_default_head)
                .into(ivHead);
        uploadFileToServer(uri);
    }

    private void uploadFileToServer(Uri uri) {
        showProgressDialog(R.string.hint_updating);
        final String filePath = FileUtils.getFilePathByUri(CoreManager.getContext(), uri);
        if (filePath == null) {
            LogPrint.e("filePath is null");
            return;
        }
        File file = new File(filePath);
        BmobManager.getInstance().uploadFile(file, new DefaultCallback<String>() {
            @Override
            public void onSuccess(String value) {
                mPicPath = value;
                dismissProgressDialog();
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                showToast(errorMsg);
                dismissProgressDialog();
            }
        });
    }
}
