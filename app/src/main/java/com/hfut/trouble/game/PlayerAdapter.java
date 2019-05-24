package com.hfut.trouble.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hfut.gamelibrary.Game;
import com.hfut.trouble.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class PlayerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Game.Player> playerList;

    public PlayerAdapter(LayoutInflater inflater, List<Game.Player> playerList) {
        this.inflater = inflater;
        this.playerList = playerList;
    }

    @Override
    public int getCount() {
        return playerList.size();
    }

    @Override
    public Object getItem(int position) {
        return playerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<Game.Player> playerList){
        this.playerList = playerList;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.item_lv_player, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvId.setText(playerList.get(position).getUserId());
        viewHolder.tvPoint.setText("" + playerList.get(position).getPoint());
        viewHolder.tvBlood.setText("" + playerList.get(position).getBlood());
        StringBuilder cardList = new StringBuilder();
        for(Game.Card card:playerList.get(position).getCardList()){
            if(card == null){
                continue;
            }
            cardList.append(card.toString());
            cardList.append(" ");
        }
        viewHolder.tvCardList.setText(cardList.toString());

        return convertView;
    }
    class ViewHolder{
        @BindView(R.id.tv_player_id)
        TextView tvId;
        @BindView(R.id.tv_player_point)
        TextView tvPoint;
        @BindView(R.id.tv_player_blood)
        TextView tvBlood;
        @BindView(R.id.tv_card)
        TextView tvCardList;
        ViewHolder(View v){
            ButterKnife.bind(this, v);
        }
    }
}
