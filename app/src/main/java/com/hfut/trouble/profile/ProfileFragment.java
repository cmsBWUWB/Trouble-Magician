package com.hfut.trouble.profile;

import android.app.Activity;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hfut.base.application.CoreManager;
import com.hfut.base.dialog.CameraHandler;
import com.hfut.base.fragment.BaseFragment;
import com.hfut.base.model.CameraImageBean;
import com.hfut.base.model.RequestCodes;
import com.hfut.base.manager.IMManager;
import com.hfut.base.manager.UserManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.trouble.LoginActivity;
import com.hfut.trouble.R;
import com.hfut.utils.callbacks.DefaultCallback;
import com.hfut.utils.utils.FileUtils;
import com.socks.library.KLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import xiaoma.com.bomb.BmobManager;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.iv_head)
    ImageView iv_head;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_profile;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initHead();
        return view;
    }

    private void initHead() {
        Glide.with(this)
                .load(UserManager.INSTANCE.getUserIcon())
                .transform(new CircleCrop())
                .error(R.drawable.icon_default_head)
                .into(iv_head);
    }

    @OnClick(R.id.bt_logout)
    void logout(View view) {
        IMManager.getInstance().logout(new BaseEMCallBack() {
            @Override
            public void onSuccess() {
                super.onSuccess();
                Activity activity = getActivity();
                if (activity != null) {
                    startActivity(new Intent(activity, LoginActivity.class));
                    activity.finish();
                }
            }

            @Override
            public void onError(int code, String error) {
                super.onError(code, error);
                KLog.e("code = " + code + "; error = " + error);
            }
        });
    }

    @OnClick(R.id.iv_head)
    void changeUserHead(View view) {
        new CameraHandler(this).beginChooseDialog();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCodes.TAKE_PHOTO:
                final Uri uri = CameraImageBean.INSTANCE.getPath();
                if (uri == null) {
                    KLog.e("uri is null!");
                    return;
                }
                uploadFileToServer(uri);
                break;
            case RequestCodes.PICK_PHOTO:
                //这里的uri是content开头，需要解析成file开头的才可以正常使用
                final Uri pickUri = data.getData();
                if (pickUri == null) {
                    KLog.e("uri is null!");
                    return;
                }
                uploadFileToServer(pickUri);
                break;
            default:
                break;
        }
    }

    private void uploadFileToServer(Uri uri) {
        showProgressDialog(R.string.hint_updating);
        final String filePath = FileUtils.getFilePathByUri(CoreManager.getContext(), uri);
        if (filePath == null) {
            KLog.e("filePath is null");
            return;
        }
        File file = new File(filePath);
        BmobManager.getInstance().uploadFile(file, new DefaultCallback<String>() {
            @Override
            public void onSuccess(String value) {
                //上传成功之后，把User的数据更新
                UserManager.INSTANCE.uploadUserIcon(value, new DefaultCallback<Object>() {

                    @Override
                    public void onFail(int errorCode, @NotNull String errorMsg) {
                        //更新头像失败
                        KLog.e("errorCode = " + errorCode);
                        showToast(errorMsg);
                        dismissProgressDialog();
                    }

                    @Override
                    public void onSuccess(Object value) {
                        //更新头像成功
                        showToast(R.string.update_head_success);
                        Glide.with(ProfileFragment.this)
                                .load(file)
                                .transform(new CircleCrop())
                                .error(R.drawable.icon_default_head)
                                .into(iv_head);
                        dismissProgressDialog();
                    }
                });
            }

            @Override
            public void onFail(int errorCode, @NotNull String errorMsg) {
                showToast(errorMsg);
                dismissProgressDialog();
            }
        });
    }
}
