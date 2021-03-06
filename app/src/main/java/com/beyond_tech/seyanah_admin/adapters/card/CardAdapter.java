package com.beyond_tech.seyanah_admin.adapters.card;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.beyond_tech.seyanah_admin.holders.ItemHolder;
import com.beyond_tech.seyanah_admin.models.ItemCard;
import com.beyond_tech.seyanah_admin.utils.BaseUtils;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<ItemHolder> {

    private List<ItemCard> mCards = new ArrayList<>();
    private int mType = BaseUtils.TYPE_LIST;

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ItemHolder.newInstance(parent, mType);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(mCards.get(position));
    }

    @Override
    public int getItemCount() {
        return mCards.size();
    }

    public void setCards(List<ItemCard> cards) {
        if (cards == null) {
            return;
        }

        mCards = cards;
    }

    public void setType(int type) {
        this.mType = type;
    }
}