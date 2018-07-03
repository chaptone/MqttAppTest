package com.example.chapmac.rakkan.mqtt_app_test.Main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.TimeConverter;

import java.util.ArrayList;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionViewHolder> {

    private ArrayList<Connection> connectionList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnCilckItemListener(OnItemClickListener listener){
        mListener = listener;
    }



    public ConnectionAdapter(ArrayList<Connection> connections){
        connectionList = connections;
    }

    @NonNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_connection_item,parent,false);
        ConnectionViewHolder connectionViewHolder = new ConnectionViewHolder(view,mListener);
        return connectionViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
        Connection currentItem = connectionList.get(position);

        TimeConverter timeConverter = new TimeConverter();

//        holder.mImageView.setImageResource(currentItem.getImage());
        holder.mTextView.setText(currentItem.getName());

        String hostWithPort = currentItem.getHost()+":"+currentItem.getPort();
        holder.mTextView1.setText(hostWithPort);
    }

    @Override
    public int getItemCount() {
        return connectionList.size();
    }

}
