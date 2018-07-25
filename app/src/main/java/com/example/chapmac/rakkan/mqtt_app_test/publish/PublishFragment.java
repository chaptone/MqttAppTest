package com.example.chapmac.rakkan.mqtt_app_test.publish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;

public class PublishFragment extends Fragment {

    private ArrayList<PublishItem> publishList;
    private PublishAdapter publishAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // collectionReference is the path for store every publish in database.
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PREFER.getConnection().getId()).collection("publish");

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

        // Retrieve every publish from database and show in list.
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
                                PublishItem publishItem = documentSnapshot.getDocument().toObject(PublishItem.class);
                                publishItem.setDocumentId(documentSnapshot.getDocument().getId());
                                publishList.add(0,publishItem);
                            }
                        }
                        publishAdapter.notifyItemInserted(0);
                        publishAdapter.notifyDataSetChanged();
                    }
                });

        // List click handle.
        publishAdapter.setOnCilckItemListener(new PublishAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) { }

            // List delete bin icon click handle.
            @Override
            public void onDeleteClick(int position) {

                // Remove publish history from database.
                collectionReference.document(publishList.get(position).getDocumentId()).delete();

                // Remove publish history from list.
                publishList.remove(position);
                publishAdapter.notifyItemRemoved(position);
            }
        });

        return view;
    }

    // Add publish to database.
    public void addPublisher(String topic,String message){

        // Put time stamp in every subscribe.
        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss EEE:MMM W")
                .format(Calendar.getInstance().getTime());

        PublishItem publishItem = new PublishItem(topic, message, currentDate);
        addToDatabase(publishItem);
    }

    private void addToDatabase(PublishItem publishItem) {
        collectionReference.add(publishItem);
    }

}
