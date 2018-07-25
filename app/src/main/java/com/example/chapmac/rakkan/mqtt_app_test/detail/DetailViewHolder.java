package com.example.chapmac.rakkan.mqtt_app_test.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.home.HomeAdapter;

// This class is required by android for list.
public class DetailViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView;
        public TextView mTextView1;

        public DetailViewHolder(View itemView){
            super(itemView);

            mTextView = itemView.findViewById(R.id.textView);
            mTextView1 = itemView.findViewById(R.id.textView1);

        }
    }