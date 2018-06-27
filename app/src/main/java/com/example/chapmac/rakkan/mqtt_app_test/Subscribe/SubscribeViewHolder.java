package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.R;

public class SubscribeViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mDeleteImage;

        public SubscribeViewHolder(View itemView, final SubscribeAdapter.OnItemClickListener listener){
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView1);

            mDeleteImage = itemView.findViewById(R.id.image_delete);

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

            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }