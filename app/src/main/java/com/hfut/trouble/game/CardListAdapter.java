package com.hfut.trouble.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hfut.gamelibrary.Game;
import com.hfut.trouble.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardListAdapter extends RecyclerView.Adapter {
    private List<Game.Card> cardList = new ArrayList<>();
    private LayoutInflater inflater;

    public CardListAdapter(LayoutInflater inflater){
        this.inflater = inflater;
    }

    public void setData(List<Game.Card> cardList){
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.item_rv_card_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.ivCard.setImageResource(getCardResId(cardList.get(position)));
    }
    private int getCardResId(Game.Card card){
        int result = 0;
        switch (card){
            case DRAGON:
                result = R.mipmap.card_01_dragon;
                break;
            case DARK:
                result = R.mipmap.card_02_dark;
                break;
            case DREAM:
                result = R.mipmap.card_03_dream;
                break;
            case CAT_BIRD:
                result = R.mipmap.card_04_cat_bird;
                break;
            case FLASH:
                result = R.mipmap.card_05_flash;
                break;
            case SNOW:
                result = R.mipmap.card_06_snow;
                break;
            case FIRE:
                result = R.mipmap.card_07_fire;
                break;
            case BLOOD:
                result = R.mipmap.card_08_blood;
                break;
        }
        return result;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_card)
        ImageView ivCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
