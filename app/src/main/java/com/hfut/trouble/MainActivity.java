package com.hfut.trouble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
    }

    public void changeFragment(View v){
        switch (v.getId()){
            case R.id.tv_game:
                getSupportFragmentManager().beginTransaction()
                        .show(gameFragment)
                        .hide(sociaFragment)
                        .hide(profileFragment)
                        .commit();
                break;
            case R.id.tv_socia:
                getSupportFragmentManager().beginTransaction()
                        .hide(gameFragment)
                        .show(sociaFragment)
                        .hide(profileFragment)
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
