package com.example.chapmac.rakkan.mqtt_app_test.Publish;


import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.Subscribe.SubscribeItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.annotation.Nullable;

public class PublishFragment extends Fragment {

    private ArrayList<PublishItem> publishList;
    private PublishAdapter publishAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database");

    private String aId;

    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);
        aId = Settings.Secure.getString(getActivity().getContentResolver(),Settings.Secure.ANDROID_ID);

        publishList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        publishAdapter = new PublishAdapter(publishList);
        recyclerView.setAdapter(publishAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        collectionReference.document(aId).collection("pub").orderBy("time")
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

                                publishList.add(publishItem);
                            }
                        }
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
                collectionReference.document(aId).collection("pub").document(publishList.get(position).getDocumentId()).delete();
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
        collectionReference.document(aId).collection("pub").add(publishItem);
    }

}
