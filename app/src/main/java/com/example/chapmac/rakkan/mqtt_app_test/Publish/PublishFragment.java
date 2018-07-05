package com.example.chapmac.rakkan.mqtt_app_test.Publish;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

import static com.example.chapmac.rakkan.mqtt_app_test.Main.SplashActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.Main.SplashActivity._PERF;

public class PublishFragment extends Fragment {

    private ArrayList<PublishItem> publishList;
    private PublishAdapter publishAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PERF.getConnection().getId()).collection("publish");

    private PullRefreshLayout layout;

    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        publishList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        publishAdapter = new PublishAdapter(publishList);
        recyclerView.setAdapter(publishAdapter);

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
                        publishAdapter.notifyDataSetChanged();
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
                                PublishItem publishItem = documentSnapshot.getDocument().toObject(PublishItem.class);
                                publishItem.setDocumentId(documentSnapshot.getDocument().getId());
                                publishList.add(0,publishItem);
                            }
                        }
                        publishAdapter.notifyItemInserted(0);
                        publishAdapter.notifyDataSetChanged();
                    }
                });

        publishAdapter.setOnCilckItemListener(new PublishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                publishList.get(position).setMessage("Click");
//                publishAdapter.notifyItemChanged(position);
            }
            @Override
            public void onDeleteClick(int position) {
                collectionReference.document(publishList.get(position).getDocumentId()).delete();
                publishList.remove(position);
                publishAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    public void addPublisher(String topic,String message){
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss EEE:MMM W")
                .format(Calendar.getInstance().getTime());

        PublishItem publishItem = new PublishItem(R.drawable.ic_publish_gray, topic, message, currentDate);
//        publishList.add(new PublishItem( topic, message));
//        publishAdapter.notifyItemInserted(publishList.size());
        addToDatabase(publishItem);
    }

    private void addToDatabase(PublishItem publishItem) {
        collectionReference.add(publishItem);
    }

}
