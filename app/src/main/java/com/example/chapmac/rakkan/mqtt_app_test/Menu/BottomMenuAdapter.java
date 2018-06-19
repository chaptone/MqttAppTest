package com.example.chapmac.rakkan.mqtt_app_test.Menu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeViewHolder;

import java.util.ArrayList;

public class BottomMenuAdapter extends RecyclerView.Adapter<BottomMenuViewHolder> {

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

    @NonNull
    @Override
    public BottomMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_bottom_menu_item,parent,false);
        BottomMenuViewHolder bottomMenuViewHolder = new BottomMenuViewHolder(view,mListener);
        return bottomMenuViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BottomMenuViewHolder holder, int position) {
        BottomMenuItem currentItem = bottomMenuList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
    }

    @Override
    public int getItemCount() {
        return bottomMenuList.size();
    }

}
