package com.hfut.trouble.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfut.imlibrary.model.User;
import com.hfut.trouble.R;

import java.util.List;

/**
 * Created by wzt on 2019/5/22
 */
public class GameRoomMemberAdatper extends BaseAdapter {
    private List<User> users;
    private Context mContext;

    public GameRoomMemberAdatper(Context context, List<User> users) {
        mContext = context;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return users.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = users.get(position);
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_lv_room_member, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        viewHolder.textView.setText(user.getUserId());
        return convertView;
    }

    class ViewHolder{
        TextView textView;

        public ViewHolder(View itemView) {
            this.textView = itemView.findViewById(R.id.tv_user_name);
        }
    }
}
