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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.chapmac.rakkan.mqtt_app_test.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

import static com.example.chapmac.rakkan.mqtt_app_test.Notification.CHANNEL_1_ID;
import static com.example.chapmac.rakkan.mqtt_app_test.Main.SplashActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.Main.SplashActivity._PERF;


public class HomeFragment extends Fragment {

    private NotificationManagerCompat notificationManager;

    private ArrayList<HomeItem> homeList;
    private HomeAdapter homeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PERF.getConnection().getId()).collection("home");

    private PullRefreshLayout layout;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        notificationManager = NotificationManagerCompat.from(getActivity());

        homeList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        homeAdapter = new HomeAdapter(homeList);
        recyclerView.setAdapter(homeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        layout = view.findViewById(R.id.swipeRefreshLayout);

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        homeAdapter.notifyDataSetChanged();
                        layout.setRefreshing(false);
                    }
                },500);
            }
        });

        collectionReference.orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }
                        for (DocumentChange documentSnapshot : queryDocumentSnapshots.getDocumentChanges()) {
                            if (documentSnapshot.getType() == DocumentChange.Type.ADDED) {
                                HomeItem homeItem = documentSnapshot.getDocument().toObject(HomeItem.class);
                                homeItem.setDocumentId(documentSnapshot.getDocument().getId());
                                homeList.add(0,homeItem);
                            }
                        }
                        homeAdapter.notifyItemInserted(0);
                        homeAdapter.notifyDataSetChanged();
                    }
                });

        homeAdapter.setOnCilckItemListener(new HomeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
//                homeList.get(position).changeText1("Click");
//                homeAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                collectionReference.document(homeList.get(position).getDocumentId()).delete();
                homeList.remove(position);
                homeAdapter.notifyItemRemoved(position);
            }
        });

        MqttHelper.CLIENT.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                String messageStr = new String(message.getPayload());
                String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss EEE:MMM W")
                        .format(Calendar.getInstance().getTime());

                HomeItem homeItem = new HomeItem(R.drawable.ic_send_gray, topic, messageStr,currentDate);
//                homeList.add(new HomeItem(topic, messageStr));
//                homeAdapter.notifyItemInserted(homeList.size());
                addToDatabase(homeItem);


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

    private void addToDatabase(HomeItem homeItem) {
        collectionReference.add(homeItem);
    }

}
