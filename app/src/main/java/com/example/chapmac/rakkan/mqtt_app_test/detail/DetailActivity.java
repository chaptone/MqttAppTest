package com.example.chapmac.rakkan.mqtt_app_test.detail;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.example.chapmac.rakkan.mqtt_app_test.utility.MqttHelper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

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

    private ImageButton sendBtn;
    private EditText editText;
    private String topicName;
    private RecyclerView recyclerView;
    private int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        color = _PREFER.getConnection().getColor();
        sendBtn = findViewById(R.id.btn_send);
        editText = findViewById(R.id.text_send);
        Drawable drawable = ContextCompat.getDrawable
                (this, R.drawable.ic_send);
        DrawableCompat.setTint(drawable, color);
        sendBtn.setBackground(drawable);

        String topic_id = getIntent().getExtras().getString("TOPIC_ID");
        topicName = getIntent().getExtras().getString("TOPIC_NAME");

        getSupportActionBar().setTitle(topicName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


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
                            DetailItem detailItem = documentSnapshot.getDocument().toObject(DetailItem.class);
                            detailItem.setDocumentId(documentSnapshot.getDocument().getId());
                            detailList.add(detailItem);
                            detailAdapter = new DetailAdapter(detailList);
                            recyclerView.setAdapter(detailAdapter);
                        }
                        detailAdapter.notifyDataSetChanged();
                    }
                });

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.scrollToPosition(detailList.size()-1);
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sendText = editText.getText().toString().trim();
                if (!sendText.equals("")) {
                    editText.setText("");
                    publish(topicName,sendText);
                }
            }
        });
    }

    public void publish(String topicName, String sendText) {
        try {
            MqttHelper.CLIENT.publish(topicName, sendText.getBytes(),0,false)
                    .setActionCallback(new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {

                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            onBackPressed();
            return true;
        }
        return false;
    }
}
