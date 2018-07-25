package com.example.chapmac.rakkan.mqtt_app_test.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.main.LoadingActivity._PREFER;

public class DetailActivity extends AppCompatActivity {

    private ArrayList<DetailItem> detailList;
    private DetailAdapter detailAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection")
            .document(_PREFER.getConnection().getId()).collection("home");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String topic_id = getIntent().getExtras().getString("TOPIC_ID");
        String topic_name = getIntent().getExtras().getString("TOPIC_NAME");

        getSupportActionBar().setTitle(topic_name);

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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
