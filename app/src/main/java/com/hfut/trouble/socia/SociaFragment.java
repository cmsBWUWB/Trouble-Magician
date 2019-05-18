package com.hfut.trouble.socia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hfut.trouble.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SociaFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_socia, container, false);
        ButterKnife.bind(this, v);

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
