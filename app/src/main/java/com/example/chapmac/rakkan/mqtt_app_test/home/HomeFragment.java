package com.example.chapmac.rakkan.mqtt_app_test.home;


import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.utility.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.detail.DetailActivity;
import com.example.chapmac.rakkan.mqtt_app_test.detail.DetailItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
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

import static com.example.chapmac.rakkan.mqtt_app_test.utility.Notification.CHANNEL_1_ID;
import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;


public class HomeFragment extends Fragment {

    private NotificationManagerCompat notificationManager;

    private ArrayList<HomeItem> homeList;
    private HomeAdapter homeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PREFER.getConnection().getId()).collection("home");

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
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
                                homeAdapter.notifyItemInserted(0);
                                homeAdapter.notifyDataSetChanged();
                            }
                            if (documentSnapshot.getType() == DocumentChange.Type.MODIFIED) {
                                HomeItem homeItem = documentSnapshot.getDocument().toObject(HomeItem.class);
                                homeItem.setDocumentId(documentSnapshot.getDocument().getId());
                                for (int i = 0 ; i < homeList.size() ; i++){
                                    if(homeList.get(i).getTopic().equals(homeItem.getTopic())){
                                        homeList.remove(i);
                                        homeAdapter.notifyItemRemoved(i);
                                    }
                                }
                                homeList.add(0,homeItem);
                                homeAdapter.notifyItemInserted(0);
                                homeAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

        homeAdapter.setOnCilckItemListener(new HomeAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("TOPIC_ID",homeList.get(position).getDocumentId());
                intent.putExtra("TOPIC_NAME",homeList.get(position).getTopic());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

                HomeItem homeItem = new HomeItem(topic, messageStr,currentDate);
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

    private void addToDatabase(final HomeItem homeItem) {
        collectionReference.whereEqualTo("topic",homeItem.getTopic())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){
                    if(task.getResult().isEmpty()){
                        collectionReference.add(homeItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isComplete()){
                                    String topicId = task.getResult().getId();
                                    DetailItem detailItem = new DetailItem(homeItem.getMessage(),homeItem.getTime());
                                    collectionReference.document(topicId).collection("detail").add(detailItem);
                                }
                            }
                        });
                    }else{
                        final String topicId = task.getResult().getDocuments().get(0).getId();
                        DetailItem detailItem = new DetailItem(homeItem.getMessage(),homeItem.getTime());
                        collectionReference.document(topicId).collection("detail").add(detailItem).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if(task.isComplete()){
                                    collectionReference.document(topicId).set(homeItem);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

}
