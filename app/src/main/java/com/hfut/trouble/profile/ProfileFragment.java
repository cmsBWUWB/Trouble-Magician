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
import com.hfut.base.dialog.CameraHandler;
import com.hfut.base.fragment.BaseFragment;
import com.hfut.base.model.CameraImageBean;
import com.hfut.base.model.RequestCodes;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.trouble.LoginActivity;
import com.hfut.trouble.R;
import com.socks.library.KLog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.OnClick;

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
                .load("http://bmob-cdn-23873.b0.upaiyun.com/2019/05/23/5d575279400fc06d80a406ef63f3ad04.png")
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
                Glide.with(this)
                        .load(CameraImageBean.INSTANCE.getPath())
                        .transform(new CircleCrop())
                        .error(R.drawable.icon_default_head)
                        .into(iv_head);
                break;
            case RequestCodes.PICK_PHOTO:
                final Uri pickPath = data.getData();
                Glide.with(this)
                        .load(pickPath)
                        .transform(new CircleCrop())
                        .error(R.drawable.icon_default_head)
                        .into(iv_head);
                break;
            default:
                break;
        }
    }
}
