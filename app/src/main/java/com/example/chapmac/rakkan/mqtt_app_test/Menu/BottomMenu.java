package com.example.chapmac.rakkan.mqtt_app_test.Menu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeAdapter;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;

import java.util.ArrayList;

public class BottomMenu extends BottomSheetDialogFragment {

    private ArrayList<BottomMenuItem> bottomMenuItems;

    private BottomMenuAdapter bottomMenuAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_menu,container,false);

        bottomMenuItems = new ArrayList<>();
        bottomMenuItems.add(new BottomMenuItem(R.drawable.ic_local_offer, "Line1"));
        bottomMenuItems.add(new BottomMenuItem(R.drawable.ic_local_offer, "Line2"));
        bottomMenuItems.add(new BottomMenuItem(R.drawable.ic_local_offer, "Line3"));

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bottomMenuAdapter = new BottomMenuAdapter(bottomMenuItems);
        recyclerView.setAdapter(bottomMenuAdapter);

        bottomMenuAdapter.setOnCilckItemListener(new BottomMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                bottomMenuItems.get(position).setText1("Click");
                bottomMenuAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                bottomMenuItems.remove(position);
                bottomMenuAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

}
