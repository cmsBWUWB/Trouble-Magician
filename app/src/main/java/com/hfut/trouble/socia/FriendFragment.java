package com.hfut.trouble.socia;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hfut.trouble.R;

public class FriendFragment extends Fragment {
    public static FriendFragment newInstance() {

        Bundle args = new Bundle();

        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_socia_friend, container, false);
        ListView lv = v.findViewById(R.id.lv_friend);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return 30;
            }

            @Override
            public Object getItem(int i) {
                return i;
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView tv = new TextView(getContext());
                tv.setHeight(80);
                tv.setText("好友列表 " + i);
                return tv;
            }
        });
        return v;
    }
}
