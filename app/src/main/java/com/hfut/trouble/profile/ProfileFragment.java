package com.hfut.trouble.profile;

import android.app.Activity;
import android.content.Intent;

import android.util.Log;
import android.view.View;

import com.hfut.base.fragment.BaseFragment;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.trouble.LoginActivity;
import com.hfut.trouble.R;
import com.socks.library.KLog;

import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_profile;
    }

    @OnClick(R.id.bt_logout)
    void logout(View view){
        IMManager.getInstance().logout(new BaseEMCallBack(){
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
}
