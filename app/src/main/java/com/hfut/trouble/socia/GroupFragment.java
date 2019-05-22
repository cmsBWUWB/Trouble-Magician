package com.hfut.trouble.socia;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.hfut.base.fragment.BaseFragment;

public class GroupFragment extends BaseFragment {
    public static GroupFragment newInstance() {

        Bundle args = new Bundle();

        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayout() {
        return 0;
    }
}
