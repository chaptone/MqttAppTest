package com.example.chapmac.rakkan.mqtt_app_test.Home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.TimeConverter;

import java.util.ArrayList;

import static com.example.chapmac.rakkan.mqtt_app_test.Main.SplashActivity._PERF;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private ArrayList<HomeItem> homeList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnCilckItemListener(OnItemClickListener listener){
        mListener = listener;
    }



    public HomeAdapter(ArrayList<HomeItem> homeItems){
        homeList = homeItems;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_item,parent,false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view,mListener);
        return homeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        HomeItem currentItem = homeList.get(position);

        TimeConverter timeConverter = new TimeConverter();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("", _PERF.getConnection().getColor() );

        holder.mImageView.setImageDrawable(drawable);
        holder.mTextView.setText("Topic : "+currentItem.getTopic());
        holder.mTextView1.setText("Message : "+currentItem.getMessage());
        holder.mTextView2.setText(timeConverter.convertTimeFrom(currentItem.getTime()));
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

}
