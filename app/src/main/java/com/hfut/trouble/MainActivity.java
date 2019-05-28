package com.hfut.trouble;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hfut.base.activity.BaseActivity;
import com.hfut.base.manager.IMManager;
import com.hfut.imlibrary.listener.BaseEMCallBack;
import com.hfut.trouble.profile.ProfileFragment;
import com.hfut.trouble.socia.SociaFragment;
import com.socks.library.KLog;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

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
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_friend:
                showAddFriendDialog();
                break;
        }
        return true;
    }

    private void showAddFriendDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_friend, null);
        EditText etUsername = v.findViewById(R.id.et_username);
        builder.setTitle(R.string.add_friend)
                .setView(v)
                .setPositiveButton("添加好友", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String username = etUsername.getText().toString();
                        IMManager.getInstance().requestAddFriend(username,new BaseEMCallBack(){
                            @Override
                            public void onSuccess() {
                                super.onSuccess();
                                showToast(R.string.add_success_hint);
                            }

                            @Override
                            public void onError(int code, String error) {
                                super.onError(code, error);
                                KLog.e("code = " + code + "; error = " + error);
                                showToast(R.string.error_hint);
                            }
                        });
                    }
                }).show();
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
