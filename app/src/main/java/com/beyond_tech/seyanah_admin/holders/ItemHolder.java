package com.beyond_tech.seyanah_admin.holders;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.beyond_tech.seyanah_admin.models.ItemCard;


public class ItemHolder extends RecyclerView.ViewHolder {

    private TextView mTitleView;
    private TextView mDescView;
    private ImageView mThumbnailView;
    private TextView mSummaryView;

    public static ItemHolder newInstance(ViewGroup container, int type) {
        View root = LayoutInflater.from(container.getContext()).inflate(getLayoutResourceId(type),
                container, false);

        return new ItemHolder(root);
    }

    private ItemHolder(View itemView) {
        super(itemView);

//        mTitleView =  itemView.findViewById(R.id.card_title);
//        mDescView =  itemView.findViewById(R.id.card_subtitle);
//        mSummaryView =  itemView.findViewById(R.id.card_summary);
//        mThumbnailView = itemView.findViewById(R.id.card_image);
    }

    public void bind(ItemCard card) {
        mTitleView.setText(card.getTitle());
        mDescView.setText(card.getDescription());
        mSummaryView.setText(card.getSummaryText());

        Glide.with(itemView.getContext()).load(card.getThumbnailUrl()).into(mThumbnailView);
    }

    private static int getLayoutResourceId(int type) {
        int selectedLayoutResource = 0;
//        switch (type) {
//            case TYPE_LIST:
//                selectedLayoutResource = R.layout.layout_news_card;
//                break;
//            case TYPE_SECOND_LIST:
//                selectedLayoutResource = R.layout.layout_second_news_card;
//                break;
//            case TYPE_GRID:
//            case TYPE_SECOND_GRID:
//                selectedLayoutResource = R.layout.layout_ecom_item;
//                break;
//            default:
//                selectedLayoutResource = 0;
//        }

        return selectedLayoutResource;
    }
}

