package com.example.chapmac.rakkan.mqtt_app_test.Publish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapmac.rakkan.mqtt_app_test.MainActivity;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeAdapter;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class PublishFragment extends Fragment {

    private ArrayList<PublishItem> publishItems;

    private PublishAdapter publishAdapter;

    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        publishItems = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        publishAdapter = new PublishAdapter(publishItems);
        recyclerView.setAdapter(publishAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        publishAdapter.setOnCilckItemListener(new PublishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                publishItems.get(position).setMessage("Click");
                publishAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                publishItems.remove(position);
                publishAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    public void addPublisher(String topic,String message){
        publishItems.add(new PublishItem( topic, message));
        publishAdapter.notifyItemInserted(publishItems.size());
    }
}
