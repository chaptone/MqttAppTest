package com.example.chapmac.rakkan.mqtt_app_test.subscribe;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.utility.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;

public class SubscribeFragment extends Fragment {

    private ArrayList<SubscribeItem> subscribeList;
    private SubscribeAdapter subscribeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // collectionReference is the path for store every subscribe in database.
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PREFER.getConnection().getId()).collection("subscribe");

    public SubscribeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        subscribeList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscribeAdapter = new SubscribeAdapter(subscribeList);
        recyclerView.setAdapter(subscribeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Retrieve every subscribe from database and show in list.
        // .addSnapshotListener will call when something update in database so
        // this list will update simultaneously with database(real time update).
        collectionReference.orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }
                for (DocumentChange documentSnapshot : queryDocumentSnapshots.getDocumentChanges()) {
                    if (documentSnapshot.getType() == DocumentChange.Type.ADDED) {
                        SubscribeItem subscribeItem = documentSnapshot.getDocument().toObject(SubscribeItem.class);
                        subscribeItem.setDocumentId(documentSnapshot.getDocument().getId());
                        subscribeList.add(0,subscribeItem);
                    }
                    subscribeAdapter.notifyItemInserted(0);
                    subscribeAdapter.notifyDataSetChanged();
                }
            }
        });

        // List click handle.
        subscribeAdapter.setOnCilckItemListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { }

            // List delete bin icon click handle.
            @Override
            public void onDeleteClick(int position) {
                unSubscription(subscribeList.get(position).getTopic(),position);
            }
        });

        return view;
    }

    // Unsubscribe topic from MQTT broker.
    private void unSubscription(String topic, final int position) {
        final int pos = position;
        try {
            IMqttToken unsubToken = MqttHelper.CLIENT.unsubscribe(topic);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    // If success also delete from database too.
                    collectionReference.document(subscribeList.get(position).getDocumentId()).delete();

                    // Delete from list.
                    subscribeList.remove(pos);
                    subscribeAdapter.notifyItemRemoved(pos);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // Add subscribe to database.
    public void addSubscription(String topic) {

        // Put time stamp in every subscribe.
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss EEE:MMM W")
                .format(Calendar.getInstance().getTime());

        SubscribeItem subscribeItem = new SubscribeItem(topic, currentDate);
        addToDatabase(subscribeItem);
    }

    private void addToDatabase(SubscribeItem item) {
        collectionReference.add(item);
    }

    // This method will call only once when this fragment(Subscribe tab) was created.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Load subscribeItem from data base and subscribe them all.
        collectionReference.orderBy("time")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    SubscribeItem subscribeItem = documentSnapshot.toObject(SubscribeItem.class);

                    final String subTopic = subscribeItem.getTopic();

                    try {
                        IMqttToken subToken = MqttHelper.CLIENT.subscribe(subTopic, 1);
                        subToken.setActionCallback(new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {

                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken,
                                                  Throwable exception) {
                                // The subscription could not be performed, maybe the user was not
                                // authorized to subscribe on the specified topic e.g. using wildcards
                                Log.i("Check","Fail to sub : "+ subTopic);
                            }
                        });
                    } catch (MqttException e1) {
                        e1.printStackTrace();
                    }

                }
            }
        });
    }

}
