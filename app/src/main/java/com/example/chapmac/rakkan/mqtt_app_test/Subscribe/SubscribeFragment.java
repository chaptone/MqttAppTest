package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.TabActivity;

import java.util.ArrayList;

public class SubscribeFragment extends Fragment {

    private ArrayList<SubscribeItem> subscribeItems;

    private SubscribeAdapter subscribeAdapter;

    public SubscribeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        subscribeItems = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscribeAdapter = new SubscribeAdapter(subscribeItems);
        recyclerView.setAdapter(subscribeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        subscribeAdapter.setOnCilckItemListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                subscribeItems.get(position).changeText1("Click");
                subscribeAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                subscribeItems.remove(position);
                subscribeAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    public void addSub(String topic) {
        subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, topic, "Line2"));
        subscribeAdapter.notifyItemInserted(subscribeItems.size());
    }

}
