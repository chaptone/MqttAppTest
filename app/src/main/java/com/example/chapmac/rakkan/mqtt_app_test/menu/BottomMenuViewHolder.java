package com.example.chapmac.rakkan.mqtt_app_test.menu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.R;

public class BottomMenuViewHolder extends RecyclerView.ViewHolder{

        public TextView mTextView1;

        public BottomMenuViewHolder(View itemView, final BottomMenuAdapter.OnItemClickListener listener){
            super(itemView);
            mTextView1 = itemView.findViewById(R.id.textView);

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