package com.example.chapmac.rakkan.mqtt_app_test.detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.TimeConverter;
import com.example.chapmac.rakkan.mqtt_app_test.home.HomeItem;
import com.example.chapmac.rakkan.mqtt_app_test.home.HomeViewHolder;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    private ArrayList<DetailItem> detailList;

    public DetailAdapter(ArrayList<DetailItem> detailItems){
        detailList = detailItems;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_item,parent,false);
        DetailViewHolder detailViewHolder = new DetailViewHolder(view);
        return detailViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        DetailItem currentItem = detailList.get(position);

        TimeConverter timeConverter = new TimeConverter();

        holder.mTextView.setText("Message : "+currentItem.getMessage());
        holder.mTextView1.setText(timeConverter.convertTimeFrom(currentItem.getTime()));
    }

    @Override
    public int getItemCount() {
        return detailList.size();
    }

}
