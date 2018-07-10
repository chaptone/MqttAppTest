package com.example.chapmac.rakkan.mqtt_app_test.main;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.R;

public class ConnectionViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextView1;
        public ImageView mDeleteImage;

        public ConnectionViewHolder(View itemView, final ConnectionAdapter.OnItemClickListener listener){
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
            mTextView = itemView.findViewById(R.id.textView);
            mTextView1 = itemView.findViewById(R.id.textView1);
            mDeleteImage = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
//                            view.setSelected(true);
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