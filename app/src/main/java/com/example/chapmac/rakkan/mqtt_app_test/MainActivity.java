package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
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

import com.example.chapmac.rakkan.mqtt_app_test.Main.AppConnectionPreferences;
import com.example.chapmac.rakkan.mqtt_app_test.Main.Connection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

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
    private CollectionReference collectionReference;

    public static String _ID;

    public static AppConnectionPreferences _PERF;

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i) {
                case EditorInfo.IME_ACTION_SEND:
                    proBar.setVisibility(View.VISIBLE);
                    if(!validateHost() | !validatePort()){
                        break;
                    }
                    connectTo(new Connection("",host,port,user,pass));
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _ID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        collectionReference = db.collection("database")
                .document(_ID).collection("connection");
        _PERF = new AppConnectionPreferences(this);

        proBar = findViewById(R.id.progressBar);
        proBar.setVisibility(View.GONE);

        if(_PERF.containsConnection()){
            proBar.setVisibility(View.VISIBLE);
            connectTo(_PERF.getConnection());
        }

        textInputLayoutHost = findViewById(R.id.textInputLayout1);
        textInputLayoutPort = findViewById(R.id.textInputLayout2);
        textInputLayoutUser = findViewById(R.id.textInputLayout3);
        textInputLayoutPass = findViewById(R.id.textInputLayout4);
        TextInputEditText textInputEditTextPass =findViewById(R.id.editText);
        textInputEditTextPass.setOnEditorActionListener(editorActionListener);


        user = textInputLayoutUser.getEditText().getText().toString();
        pass = textInputLayoutPass.getEditText().getText().toString();

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proBar.setVisibility(View.VISIBLE);
                if(!validateHost() | !validatePort()){
                    return;
                }
                connectTo(new Connection("",host,port,user,pass));
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

    public void connectTo(final Connection connection){
        CLIENT = new MqttAndroidClient(this.getApplicationContext(),
                "tcp://" + connection.getHost() + ":" + connection.getPort(),
                MqttClient.generateClientId());

        OPTIONS = new MqttConnectOptions();
        OPTIONS.setAutomaticReconnect(true);
        OPTIONS.setUserName(connection.getUser());
        OPTIONS.setPassword(connection.getPass().toCharArray());
        try {
            IMqttToken token = CLIENT.connect(OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    collectionReference.whereEqualTo("id",connection.getId())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(task.getResult().isEmpty()){
                                    Log.d("Check", "empty");
                                    pushNewConnection(connection);
                                    setCurrentConnect(connection);
                                    openNextActivity();
                                }else{
                                    Log.d("Check", "not empty");
                                    openNextActivity();
                                }

                            }
                        }
                    });
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

    public void pushNewConnection(Connection connection){
        Log.i("Check","pushNewCall");
        String id = collectionReference.document().getId();
        connection.setConnected(true);
        connection.setId(id);
        collectionReference.document(id).set(connection);
    }

    public void setCurrentConnect(Connection connection){
        _PERF.edit().putConnection(connection).apply();
    }

    public void openNextActivity(){
        Log.i("Check","OpenNextCall");
        Intent intent = new Intent(MainActivity.this, TabActivity.class);
        startActivity(intent);
        proBar.setVisibility(View.GONE);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
