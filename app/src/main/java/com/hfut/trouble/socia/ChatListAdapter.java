package com.hfut.trouble.socia;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfut.imlibrary.model.Chat;
import com.hfut.trouble.R;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Chat> chatList;

    public ChatListAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        chatList = new ArrayList<>();
    }

    public void setData(List<Chat> chatList) {
        this.chatList = chatList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Chat getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lv_chatlist, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvUsername = convertView.findViewById(R.id.tv_username);
            viewHolder.tvMessage = convertView.findViewById(R.id.tv_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置每一行消息的标题与内容，标题就显示对方的用户名或者群名，内容就显示最新的消息
        Chat chat = chatList.get(position);
        String title = chat.getTargetName();
        String lastMessage = chat.getLastMessage();

        viewHolder.tvUsername.setText(title);
        viewHolder.tvMessage.setText(lastMessage);

        return convertView;
    }

    private class ViewHolder {
        TextView tvUsername;
        TextView tvMessage;
    }
}
