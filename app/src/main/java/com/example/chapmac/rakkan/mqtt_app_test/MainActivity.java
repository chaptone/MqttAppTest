package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_HOST = "com.example.chapmac.rakkan.mqtt_app_test.EXTRA_HOST";

    private TextInputLayout textInputLayoutHost;
    private TextInputLayout textInputLayoutPort;
    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPass;

    private String host_root;
    private String host;
    private String port;
    private String user;
    private String pass;
    private boolean status = false;

    private ProgressBar proBar;

    public static MqttAndroidClient CLIENT;
    public static MqttConnectOptions OPTIONS;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("database");

    private String aId;
    private Connection connection;

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i) {
                case EditorInfo.IME_ACTION_SEND:
                    connect();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aId = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

//        if(savedInstanceState != null){
//            Log.i("Check","not null");
//        }

        collectionReference.document(aId)
                .collection("con")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Connection connectionDB = documentSnapshot.toObject(Connection.class);
                    Log.i("Check","Find connection" + connectionDB.getHost());
                    if(connectionDB.getStatus().equals("on")){
                        connection = connectionDB;
                        host_root = "tcp://" + connection.getHost() + ":" + connection.getPort();
                        String clientId = MqttClient.generateClientId();
                        CLIENT = new MqttAndroidClient(getApplicationContext(), host_root, clientId);

                        OPTIONS = new MqttConnectOptions();
                        OPTIONS.setAutomaticReconnect(true);
                        OPTIONS.setUserName(connection.getUser());
                        OPTIONS.setPassword(connection.getPass().toCharArray());
                        try {
                            IMqttToken token = CLIENT.connect(OPTIONS);
                            token.setActionCallback(new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    Intent intent = new Intent(MainActivity.this, TabActivity.class);
                                    startActivity(intent);
                                    proBar.setVisibility(View.GONE);
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    proBar.setVisibility(View.GONE);
                                    StyleableToast.makeText(MainActivity.this, "Connected Failed", R.style.toastWrong).show();
                                }
                            });
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        proBar = findViewById(R.id.progressBar);
        proBar.setVisibility(View.GONE);

        textInputLayoutHost = findViewById(R.id.textInputLayout1);
        textInputLayoutPort = findViewById(R.id.textInputLayout2);
        textInputLayoutUser = findViewById(R.id.textInputLayout3);
        textInputLayoutPass = findViewById(R.id.textInputLayout4);
        TextInputEditText textInputEditTextPass =findViewById(R.id.editText);
        textInputEditTextPass.setOnEditorActionListener(editorActionListener);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connect();
            }
        });
    }

    public boolean validateHost() {
        host = textInputLayoutHost.getEditText().getText().toString().trim();

        if (host.isEmpty()) {
            textInputLayoutHost.setError("Host can't be empty");
            return false;
        } else {
            textInputLayoutHost.setError(null);
            return true;
        }
    }

    public boolean validatePort() {
        port = textInputLayoutPort.getEditText().getText().toString().trim();

        if (host.isEmpty()) {
            textInputLayoutPort.setError("Port can't be empty");
            return false;
        } else {
            textInputLayoutPort.setError(null);
            return true;
        }
    }

    public void connect() {
//        if(!isConnected()){
//            if(!validateHost() | !validatePort()){
//                return;
//            }
//        }
        Log.i("Check","Connect call ");
        proBar.setVisibility(View.VISIBLE);

        user = textInputLayoutUser.getEditText().getText().toString();
        pass = textInputLayoutPass.getEditText().getText().toString();

        connection = new Connection();
        connection.setHost(host);
        connection.setPort(port);
        connection.setUser(user);
        connection.setPass(pass);

        host_root = "tcp://" + connection.getHost() + ":" + connection.getPort();
        String clientId = MqttClient.generateClientId();
        CLIENT = new MqttAndroidClient(this.getApplicationContext(), host_root, clientId);

        OPTIONS = new MqttConnectOptions();
        OPTIONS.setAutomaticReconnect(true);
        OPTIONS.setUserName(connection.getUser());
        OPTIONS.setPassword(connection.getPass().toCharArray());
        try {
            IMqttToken token = CLIENT.connect(OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    String id = collectionReference.document(aId).collection("con").document().getId();
                    connection.setStatus("on");
                    connection.setId(id);
                    collectionReference.document(aId).collection("con").document(id).set(connection);
//                    connection = new Connection(host,port,user,pass);
                    Intent intent = new Intent(MainActivity.this, TabActivity.class);
                    startActivity(intent);
                    proBar.setVisibility(View.GONE);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    proBar.setVisibility(View.GONE);
                    StyleableToast.makeText(MainActivity.this, "Connected Failed", R.style.toastWrong).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("connection", Parcels.wrap(connection));

        Log.i("Check","saveeee "+connection.getHost());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        connection = Parcels.unwrap(savedInstanceState.getParcelable("connection"));
        Log.i("Check","Host "+ connection);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("Check","Stoppp");
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.i("Check","Pauseeee");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Check","Deadddd");
    }
}
