package com.example.chapmac.rakkan.mqtt_app_test.menu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import java.util.ArrayList;

import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;

public class BottomMenuAdapter extends RecyclerView.Adapter {

    private ArrayList<BottomMenuItem> bottomMenuList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnCilckItemListener(OnItemClickListener listener){
        mListener = listener;
    }

    public BottomMenuAdapter(ArrayList<BottomMenuItem> bottomMenuItems){
        bottomMenuList = bottomMenuItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bottom_menu_item, parent, false);
                return new BottomMenuViewHolder(view,mListener);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bottom_menu_large_item, parent, false);
                return new BottomMenuViewHolderLarge(view,mListener);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (bottomMenuList.get(position).getType()) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BottomMenuItem object = bottomMenuList.get(position);
        if (object != null) {
            switch (object.getType()) {
                case 0:
                    ((BottomMenuViewHolder) holder).mTextView1.setText(object.getText1());
                    break;
                case 1:
                    String letter = _PREFER.getConnection().getName().substring(0,1).toUpperCase();
                    int color = _PREFER.getConnection().getColor();
                    TextDrawable drawable = TextDrawable.builder().buildRound(letter, color);
                    ((BottomMenuViewHolderLarge) holder).mImageView.setImageDrawable(drawable);
                    ((BottomMenuViewHolderLarge) holder).mTextView1.setText(object.getText1());
                    ((BottomMenuViewHolderLarge) holder).mTextView2.setText(object.getText2());
                    ((BottomMenuViewHolderLarge) holder).mDeleteImage.setImageResource(object.getImageResource1());
            }
        }
    }

    @Override
    public int getItemCount() {
        return bottomMenuList.size();
    }

}
