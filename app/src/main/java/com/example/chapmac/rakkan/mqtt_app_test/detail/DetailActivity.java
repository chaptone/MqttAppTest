package com.example.chapmac.rakkan.mqtt_app_test.detail;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.home.HomeAdapter;
import com.example.chapmac.rakkan.mqtt_app_test.home.HomeItem;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.example.chapmac.rakkan.mqtt_app_test.main.SplashActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.main.SplashActivity._PERF;

public class DetailActivity extends AppCompatActivity {

    private ArrayList<DetailItem> detailList;
    private DetailAdapter detailAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PERF.getConnection().getId()).collection("home");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        String topic_id = getIntent().getExtras().getString("TOPIC_ID");


        detailList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        detailAdapter = new DetailAdapter(detailList);
        recyclerView.setAdapter(detailAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        collectionReference.document(topic_id).collection("detail")
                .orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }
                        for (DocumentChange documentSnapshot : queryDocumentSnapshots.getDocumentChanges()) {
                            if (documentSnapshot.getType() == DocumentChange.Type.ADDED) {
                                DetailItem detailItem = documentSnapshot.getDocument().toObject(DetailItem.class);
                                detailItem.setDocumentId(documentSnapshot.getDocument().getId());
                                detailList.add(0,detailItem);
                                detailAdapter.notifyItemInserted(0);
                                detailAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

}
