package com.example.chapmac.rakkan.mqtt_app_test.subscribe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.utility.TimeConverter;

import java.util.ArrayList;

public class SubscribeAdapter extends RecyclerView.Adapter<SubscribeViewHolder> {

    private ArrayList<SubscribeItem> subscribeList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnCilckItemListener(OnItemClickListener listener){
        mListener = listener;
    }

    public SubscribeAdapter(ArrayList<SubscribeItem> subscribeItems){
        subscribeList = subscribeItems;
    }

    @NonNull
    @Override
    public SubscribeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_subscribe_item,parent,false);
        SubscribeViewHolder subscribeViewHolder = new SubscribeViewHolder(view,mListener);
        return subscribeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubscribeViewHolder holder, int position) {
        SubscribeItem currentItem = subscribeList.get(position);

        TimeConverter timeConverter = new TimeConverter();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", ColorGenerator.MATERIAL.getColor(currentItem.getTopic()));

        holder.mImageView.setImageDrawable(drawable);
        holder.mTextView1.setText("Topic : "+currentItem.getTopic());
        holder.mTextView2.setText(timeConverter.convertTimeFrom(currentItem.getTime()));
    }

    @Override
    public int getItemCount() {
        return subscribeList.size();
    }

}
