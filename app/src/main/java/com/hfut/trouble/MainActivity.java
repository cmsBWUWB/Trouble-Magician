package com.hfut.trouble;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hfut.imlibrary.IMManager;
import com.hfut.trouble.socia.SociaFragment;
import com.hfut.utils.thread.BusinessRunnable;
import com.hfut.utils.thread.ThreadDispatcher;
import com.hfut.utils.utils.ToastUtils;

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
                        ThreadDispatcher.getInstance().postToBusinessThread(new BusinessRunnable() {
                            @Override
                            public void doWorkInRun() {
                                String username = etUsername.getText().toString();
                                boolean success = IMManager.getInstance().requestAddFriend(username);
                                ThreadDispatcher.getInstance().postToMainThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.Companion.show(MainActivity.this, success ? "请求添加成功，等待对方同意" : "请求失败，请检查网络", Toast.LENGTH_SHORT);
                                    }
                                });
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
