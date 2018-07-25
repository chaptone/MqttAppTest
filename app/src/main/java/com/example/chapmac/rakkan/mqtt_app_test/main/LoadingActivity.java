package com.example.chapmac.rakkan.mqtt_app_test.main;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.chapmac.rakkan.mqtt_app_test.utility.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class LoadingActivity extends AppCompatActivity {

    public static String _ID;
    public static AppConnectionPreferences _PREFER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // initial unique android id for separate each unique user in database.
        _ID = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        _PREFER = new AppConnectionPreferences(this);


        if(_PREFER.containsConnection()){
            connectTo(_PREFER.getConnection());
        }else{
            openNextActivity();
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
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    _PREFER.edit().removeConnection().apply();
                    openNextActivity();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void openNextActivity(){
        Intent intent = new Intent(LoadingActivity.this, ConnectionActivity.class);
        startActivity(intent);
    }
}
