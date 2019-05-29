package com.hfut.trouble.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hfut.gamelibrary.Game;
import com.hfut.trouble.R;

import java.util.List;

import butterknife.BindView;
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

    public void setData(List<Game.Player> playerList) {
        this.playerList = playerList;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_lv_player, parent, false);
            viewHolder = new ViewHolder(convertView);
            LinearLayoutManager llm = new LinearLayoutManager(parent.getContext());
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            viewHolder.rvCardList.setLayoutManager(llm);
            viewHolder.rvCardList.setAdapter(new CardListAdapter(inflater));

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Game.Player player = playerList.get(position);
        viewHolder.tvPlayerName.setText(player.getUserId());
        viewHolder.tvPlayerPoint.setText(parent.getContext().getString(R.string.player_point, Integer.toString(player.getPoint())));
        viewHolder.bvBlood.setCurrentBlood(player.getBlood());
        ((CardListAdapter) viewHolder.rvCardList.getAdapter()).setData(player.getCardList());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.tv_player_name)
        TextView tvPlayerName;
        @BindView(R.id.tv_player_point)
        TextView tvPlayerPoint;
        @BindView(R.id.bv_blood)
        BloodView bvBlood;
        @BindView(R.id.rv_card_list_player)
        RecyclerView rvCardList;

        ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }
}
