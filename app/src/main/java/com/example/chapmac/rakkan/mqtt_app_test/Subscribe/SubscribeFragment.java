package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;


import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chapmac.rakkan.mqtt_app_test.MainActivity;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

import static android.support.constraint.Constraints.TAG;

public class SubscribeFragment extends Fragment {

    private ArrayList<SubscribeItem> subscribeList;
    private SubscribeItem subscribeItem;
    private SubscribeAdapter subscribeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database");

    private String aId;

    public SubscribeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
        aId = Settings.Secure.getString(getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);

        subscribeList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscribeAdapter = new SubscribeAdapter(subscribeList);
        recyclerView.setAdapter(subscribeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        collectionReference.document(aId).collection("sub").orderBy("description")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }
                for (DocumentChange documentSnapshot : queryDocumentSnapshots.getDocumentChanges()) {
                    if(documentSnapshot.getType() == DocumentChange.Type.REMOVED){

                    }else {
                        SubscribeItem subscribeItem = documentSnapshot.getDocument().toObject(SubscribeItem.class);
                        subscribeItem.setDocumentId(documentSnapshot.getDocument().getId());
                        subscribeList.add(subscribeItem);
                    }
                }
                subscribeAdapter.notifyDataSetChanged();
            }
        });

        subscribeAdapter.setOnCilckItemListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                subscribeList.get(position).changeText1("Click");
                subscribeAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                collectionReference.document(aId).collection("sub").document(subscribeList.get(position).getDocumentId()).delete();
                subscribeList.remove(position);
                subscribeAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    public void addSub(String topic) {
        Calendar calender = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy H:mm:ss EEE:MMM W");
        String currentDate = formatter.format(calender.getTime());

        subscribeItem = new SubscribeItem(R.drawable.ic_local_offer, topic, currentDate);
//        subscribeList.add(subscribeItem);
//        subscribeAdapter.notifyItemInserted(subscribeList.size());
        addToDatabase(subscribeItem);
    }

    private void addToDatabase(SubscribeItem item) {
        collectionReference.document(aId).collection("sub").add(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        collectionReference.document(Settings.Secure.getString(getActivity().getContentResolver(),Settings.Secure.ANDROID_ID))
                .collection("sub").orderBy("description")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            SubscribeItem subscribeItem = documentSnapshot.toObject(SubscribeItem.class);

                            final String subTopic = subscribeItem.getTopic();

                            try {
                                IMqttToken subToken = MainActivity.CLIENT.subscribe(subTopic, 1);
                                subToken.setActionCallback(new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        Log.i("Check","Success sub to : "+ subTopic);
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
