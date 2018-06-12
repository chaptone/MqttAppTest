package com.example.chapmac.rakkan.mqtt_app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class SubscribeFragment extends Fragment {
    private ArrayList<SubscribeItem> subscribeItems;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        subscribeItems = new ArrayList<>();
        subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, "Line1", "Line2"));


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new Adapter(subscribeItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        return view;
    }

    public void addSub(String topic) {
        Toast.makeText(getActivity(), "addSub Calllllll + "+topic, Toast.LENGTH_LONG).show();
        subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, topic, "Line2"));
        adapter.notifyDataSetChanged();

    }
}
