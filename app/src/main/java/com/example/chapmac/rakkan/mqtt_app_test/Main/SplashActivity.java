package com.example.chapmac.rakkan.mqtt_app_test.Main;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.chapmac.rakkan.mqtt_app_test.Main.*;
import com.example.chapmac.rakkan.mqtt_app_test.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
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

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        _ID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
        collectionReference = db.collection("database")
                .document(_ID).collection("connection");
        _PERF = new AppConnectionPreferences(this);

        ImageView imageView = findViewById(R.id.imageView);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(1200);
        animationDrawable.start();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(_PERF.containsConnection()){
//                  _PERF.edit().removeConnection().apply();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connectTo(_PERF.getConnection());
                        }
                    },5000);

                }else{
                    Intent intent = new Intent(SplashActivity.this, ConnectionActivity.class);
                    startActivity(intent);
                }
            }
        },3000);

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
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    _PERF.edit().removeConnection().apply();
                    openNextActivity();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void openNextActivity(){
        Intent intent = new Intent(SplashActivity.this, ConnectionActivity.class);
        startActivity(intent);
    }
}
