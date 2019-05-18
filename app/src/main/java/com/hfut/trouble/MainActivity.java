package com.hfut.trouble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hfut.imlibrary.IMManager;
import com.hfut.imlibrary.OperateCallBack;
import com.hfut.trouble.socia.SociaFragment;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_game)
    public TextView tvGame;
    @BindView(R.id.tv_socia)
    public TextView tvSocia;
    @BindView(R.id.tv_profile)
    public TextView tvProfile;

    GameFragment gameFragment;
    SociaFragment sociaFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        gameFragment = GameFragment.newInstance();
        sociaFragment = SociaFragment.newInstance();
        profileFragment = ProfileFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fl_fragment_root, gameFragment).show(gameFragment)
                .add(R.id.fl_fragment_root, sociaFragment).hide(sociaFragment)
                .add(R.id.fl_fragment_root, profileFragment).hide(profileFragment)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出即登出账号
        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
            @Override
            public void doWorkInRun() {
                IMManager.getInstance().logout(new OperateCallBack() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onFailure() {
                    }
                });
            }
        });
    }

    public void changeFragment(View v) {
        switch (v.getId()) {
            case R.id.tv_game:
                getSupportFragmentManager().beginTransaction()
                        .hide(sociaFragment)
                        .hide(profileFragment)
                        .show(gameFragment)
                        .commit();
                break;
            case R.id.tv_socia:
                getSupportFragmentManager().beginTransaction()
                        .hide(gameFragment)
                        .hide(profileFragment)
                        .show(sociaFragment)
                        .commit();
                break;
            case R.id.tv_profile:
                getSupportFragmentManager().beginTransaction()
                        .hide(gameFragment)
                        .hide(sociaFragment)
                        .show(profileFragment)
                        .commit();
                break;
        }
    }


}
