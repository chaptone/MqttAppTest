package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.chapmac.rakkan.mqtt_app_test.Menu.BottomMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Nullable;

import static com.example.chapmac.rakkan.mqtt_app_test.SplashActivity._ID;
import static com.example.chapmac.rakkan.mqtt_app_test.SplashActivity._PERF;

public class ConnectionActivity extends AppCompatActivity {

    private ArrayList<Connection> connectionList;
    private ConnectionAdapter connectionAdapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database")
            .document(_ID).collection("connection");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(_PERF.containsConnection()){
            openNextActivity();
            onPause();
            onStop();
        }
        setContentView(R.layout.activity_connection);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Connection");

        connectionList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        connectionAdapter = new ConnectionAdapter(connectionList);
        recyclerView.setAdapter(connectionAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        collectionReference.orderBy("time")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }
                        for (DocumentChange documentSnapshot : queryDocumentSnapshots.getDocumentChanges()) {
                            if (documentSnapshot.getType() == DocumentChange.Type.ADDED) {
                                Connection connection = documentSnapshot.getDocument().toObject(Connection.class);

                                connectionList.add(connection);
                            }
                        }
                        connectionAdapter.notifyDataSetChanged();
                    }
                });

        connectionAdapter.setOnCilckItemListener(new ConnectionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                connectTo(connectionList.get(position));
            }
            @Override
            public void onDeleteClick(int position) {
                deleteConnection(position);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ConnectionCreator.class);
        startActivityForResult(intent,1);
        return super.onOptionsItemSelected(item);
    }

    public void connectTo(final Connection connection){
        MqttHelper.CLIENT = new MqttAndroidClient(this.getApplicationContext(),
                "tcp://" + connection.getHost() + ":" + connection.getPort(),
                MqttClient.generateClientId());

        MqttHelper.OPTIONS = new MqttConnectOptions();
        MqttHelper.OPTIONS.setAutomaticReconnect(true);
        MqttHelper.OPTIONS.setUserName(connection.getUser());
        MqttHelper.OPTIONS.setPassword(connection.getPass().toCharArray());
        try {
            IMqttToken token = MqttHelper.CLIENT.connect(MqttHelper.OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    setCurrentConnect(connection);
                    openNextActivity();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    StyleableToast.makeText(ConnectionActivity.this, "Connected Failed", R.style.toastWrong).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentConnect(Connection connection){
        _PERF.edit().putConnection(connection).apply();
    }

    private void deleteConnection(int position) {
        collectionReference.document(connectionList.get(position).getId()).delete();
        connectionList.remove(position);
        connectionAdapter.notifyItemRemoved(position);
    }

    public void openNextActivity(){
        Intent intent = new Intent(this, TabActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1){
            if(resultCode == RESULT_OK){
                String name = data.getStringExtra("name");
                String host = data.getStringExtra("host");
                String port = data.getStringExtra("port");
                String user = data.getStringExtra("user");
                String pass = data.getStringExtra("pass");
                addConnection(new Connection(name,host,port,user,pass));
            }
        }
    }

    public void addConnection(Connection connection){
        collectionReference.add(connection).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                StyleableToast.makeText(ConnectionActivity.this, "Fail to connect database", R.style.toastWrong).show();
            }
        });
    }
}
