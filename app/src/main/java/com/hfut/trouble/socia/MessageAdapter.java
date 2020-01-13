package com.hfut.trouble.socia;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfut.imlibrary.model.Message;
import com.hfut.trouble.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private static final int TYPE_SEND = 0;
    private static final int TYPE_RECEIVE = 1;

    private LayoutInflater inflater;
    private List<Message> messageList;
    private String currentUserId;

    public MessageAdapter(LayoutInflater inflater, String currentUserId) {
        this.inflater = inflater;
        this.currentUserId = currentUserId;
        messageList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return currentUserId.equals(messageList.get(position).getAuthorId()) ? TYPE_SEND : TYPE_RECEIVE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (type == TYPE_SEND) {
                convertView = inflater.inflate(R.layout.item_lv_chat_msg_send, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_lv_chat_msg_receive, parent, false);
            }

            viewHolder.tvMessage = convertView.findViewById(R.id.tv_message);
            viewHolder.tvUserId = convertView.findViewById(R.id.tv_user_id);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUserId.setText(messageList.get(position).getAuthorId());
        viewHolder.tvMessage.setText(messageList.get(position).getContent());

        return convertView;
    }

    class ViewHolder {
        TextView tvMessage;
        TextView tvUserId;
    }

    public void setData(List<Message> messageList) {
        this.messageList = messageList;
        this.notifyDataSetChanged();
    }
}
