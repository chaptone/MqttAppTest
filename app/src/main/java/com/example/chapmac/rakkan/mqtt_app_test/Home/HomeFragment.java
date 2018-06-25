package com.example.chapmac.rakkan.mqtt_app_test.Home;


import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.MainActivity;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

import static com.example.chapmac.rakkan.mqtt_app_test.Notification.CHANNEL_1_ID;


public class HomeFragment extends Fragment {

    private NotificationManagerCompat notificationManager;

    private ArrayList<HomeItem> homeItems;

    private RecyclerView recyclerView;
    private HomeAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        notificationManager = NotificationManagerCompat.from(getActivity());

        homeItems = new ArrayList<>();
        homeItems.add(new HomeItem("Line1", "Line2"));


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new HomeAdapter(homeItems);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.setOnCilckItemListener(new HomeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                homeItems.get(position).changeText1("Click");
                adapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                homeItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        MainActivity.CLIENT.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String messageStr = new String(message.getPayload());
                homeItems.add(new HomeItem(topic, messageStr));
                adapter.notifyItemInserted(homeItems.size());

                Notification notification = new NotificationCompat.Builder(getActivity(), CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_send)
                        .setContentTitle("Received topic : "+topic)
                        .setContentText("Message : "+messageStr)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                        .build();

                notificationManager.notify(1, notification);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        return view;
    }

}
