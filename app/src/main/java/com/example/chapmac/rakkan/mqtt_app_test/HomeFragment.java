package com.example.chapmac.rakkan.mqtt_app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private ArrayList<SubscribeItem> subscribeItems;

    private RecyclerView recyclerView;
    private Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        subscribeItems = new ArrayList<>();
        subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, "Line1", "Line2"));


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new Adapter(subscribeItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        MainActivity.CLIENT.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                subscribeItems.add(new SubscribeItem(R.drawable.ic_local_offer, new String(message.getPayload()), "Line2"));
                adapter.notifyItemInserted(subscribeItems.size());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return view;
    }

}
