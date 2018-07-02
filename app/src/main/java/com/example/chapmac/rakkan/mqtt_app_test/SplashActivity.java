package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

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

public class SplashActivity extends AppCompatActivity {

    public static String _ID;
    public static AppConnectionPreferences _PERF;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;

    private ProgressBar proBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        _ID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        collectionReference = db.collection("database")
                .document(_ID).collection("connection");
        _PERF = new AppConnectionPreferences(this);

        proBar = findViewById(R.id.progressBar);
        proBar.setVisibility(View.VISIBLE);

        if(_PERF.containsConnection()){
            connectTo(_PERF.getConnection());
        }else{
            Intent intent = new Intent(SplashActivity.this, ConnectionActivity.class);
            startActivity(intent);
            proBar.setVisibility(View.GONE);
        }

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
                    openNextActivity();
                    proBar.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    proBar.setVisibility(View.GONE);
                    StyleableToast.makeText(SplashActivity.this, "Connected Failed", R.style.toastWrong).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void openNextActivity(){
        Log.i("Check","OpenNextCall");
        Intent intent = new Intent(SplashActivity.this, ConnectionActivity.class);
        startActivity(intent);
        proBar.setVisibility(View.GONE);
    }
}
