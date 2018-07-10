package com.example.chapmac.rakkan.mqtt_app_test.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.R;

public class HomeViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextView1;
        public TextView mTextView2;

        public HomeViewHolder(View itemView, final HomeAdapter.OnItemClickListener listener){
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mTextView2 = itemView.findViewById(R.id.textView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }