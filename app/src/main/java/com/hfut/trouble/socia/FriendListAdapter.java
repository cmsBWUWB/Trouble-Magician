package com.hfut.trouble.socia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfut.imlibrary.model.User;
import com.hfut.trouble.R;

import java.util.ArrayList;
import java.util.List;

public class FriendListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<User> friendList;

    public FriendListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        this.friendList = new ArrayList<>();
    }

    public void setData(List<User> friendList) {
        this.friendList = friendList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public User getItem(int i) {
        return friendList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_lv_friendlist, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.setTvUsername(view.findViewById(R.id.tv_item_friend));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.getTvUsername().setText(friendList.get(i).getUserId());

        return view;
    }

    private class ViewHolder {
        TextView tvUsername;

        TextView getTvUsername() {
            return tvUsername;
        }

        void setTvUsername(TextView tvUsername) {
            this.tvUsername = tvUsername;
        }
    }

}
