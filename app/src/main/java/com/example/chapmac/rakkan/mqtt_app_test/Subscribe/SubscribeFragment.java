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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class SubscribeFragment extends Fragment {

    private ArrayList<SubscribeItem> subscribeList;
    private SubscribeItem subscribeItem;
    private SubscribeAdapter subscribeAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("connection").document().collection("sub");

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
        Calendar calender = Calendar.getInstance();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy h:mm");
        String currentDate = formatter.format(calender.getTime());

        subscribeItem = new SubscribeItem(R.drawable.ic_local_offer, topic, currentDate);
        subscribeList.add(subscribeItem);
        subscribeAdapter.notifyItemInserted(subscribeList.size());
        addToDatabase(subscribeItem);
    }

    private void addToDatabase(SubscribeItem item) {
        collectionReference.add(item);
    }

}
