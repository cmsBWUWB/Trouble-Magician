package com.hfut.trouble.socia;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class GroupFragment extends Fragment {
    public static GroupFragment newInstance() {

        Bundle args = new Bundle();

        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
