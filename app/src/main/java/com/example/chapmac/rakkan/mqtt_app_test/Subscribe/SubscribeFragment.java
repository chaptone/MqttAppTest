package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;


import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public class SubscribeFragment extends Fragment {

    private ArrayList<SubscribeItem> subscribeList;
    private SubscribeItem subscribeItem;
    private SubscribeAdapter subscribeAdapter;

    private static final String KEY_IMAGE = "image";
    private static final String KEY_TOPIC = "topic";
    private static final String KEY_DESCRIPTION = "description";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference docRef = db.document("mqtt/connection");

    public SubscribeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        subscribeList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        subscribeAdapter = new SubscribeAdapter(subscribeList);
        recyclerView.setAdapter(subscribeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        subscribeAdapter.setOnCilckItemListener(new SubscribeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                subscribeList.get(position).changeText1("Click");
                subscribeAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                subscribeList.remove(position);
                subscribeAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    public void addSub(String topic) {
        subscribeItem = new SubscribeItem(R.drawable.ic_local_offer, topic, "Line2");
        subscribeList.add(subscribeItem);
        subscribeAdapter.notifyItemInserted(subscribeList.size());
        addToDatabase(subscribeItem);
    }

    private void addToDatabase(SubscribeItem item) {
        docRef.set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

}
