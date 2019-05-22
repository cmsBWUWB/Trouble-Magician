package com.hfut.trouble.socia;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hfut.base.fragment.BaseFragment;
import com.hfut.trouble.R;

import butterknife.BindView;

public class SociaFragment extends BaseFragment {
    @BindView(R.id.tv_chat)
    public TextView tvChat;
    @BindView(R.id.tv_friend)
    public TextView tvFriend;
    @BindView(R.id.tv_group)
    public TextView tvGroup;


    @BindView(R.id.vp_socia)
    public ViewPager vpSocia;
    public Fragment[] fragments;

    public static SociaFragment newInstance() {
        Bundle args = new Bundle();

        SociaFragment fragment = new SociaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_socia;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        initViewpager(inflater, v);
        setListener();
        return v;
    }

    private void setListener() {
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpSocia.setCurrentItem(0);
            }
        });
        tvFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpSocia.setCurrentItem(1);
            }
        });
        tvGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vpSocia.setCurrentItem(2);
            }
        });
    }

    private void initViewpager(LayoutInflater inflater, View v){
        fragments = new Fragment[3];
        fragments[0] = ChatFragment.newInstance();
        fragments[1] = FriendFragment.newInstance();
        fragments[2] = GroupFragment.newInstance();
        vpSocia.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return fragments.length;
            }
        });
    }
}
