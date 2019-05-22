package com.hfut.trouble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hfut.base.fragment.BaseFragment;
import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends BaseFragment {
    @BindView(R.id.bt_logout)
    public Button btLogout;
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
        setListener();
        return view;
    }

    private void setListener() {
        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                    @Override
                    public void doWorkInRun() {
                        IMManager.getInstance().logout(new OperateCallBack() {
                            @Override
                            public void onSuccess() {
                                ThreadDispatcher.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Activity activity = getActivity();
                                        if(activity != null){
                                            activity.finish();
                                            startActivity(new Intent(activity, LoginActivity.class));
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFailure() {
                            }
                        });
                    }
                });
            }
        });
    }
}
