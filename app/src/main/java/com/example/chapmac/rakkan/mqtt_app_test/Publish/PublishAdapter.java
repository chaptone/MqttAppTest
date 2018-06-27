package com.example.chapmac.rakkan.mqtt_app_test.Publish;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeViewHolder;
import com.example.chapmac.rakkan.mqtt_app_test.TimeConverter;

import java.util.ArrayList;

public class PublishAdapter extends RecyclerView.Adapter<PublishViewHolder> {

    private ArrayList<PublishItem> publishList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnCilckItemListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PublishAdapter(ArrayList<PublishItem> publishItems){
        publishList = publishItems;
    }

    @NonNull
    @Override
    public PublishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_publish_item,parent,false);
        PublishViewHolder publishViewHolder = new PublishViewHolder(view,mListener);
        return publishViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PublishViewHolder holder, int position) {
        PublishItem currentItem = publishList.get(position);

        TimeConverter timeConverter = new TimeConverter();

        holder.mImage.setImageResource(currentItem.getImage());
        holder.mTextView1.setText(currentItem.getTopic());
        holder.mTextView2.setText(currentItem.getMessage());
        holder.mTextView3.setText(timeConverter.convertTimeFrom(currentItem.getTime()));
    }

    @Override
    public int getItemCount() {
        return publishList.size();
    }

}
