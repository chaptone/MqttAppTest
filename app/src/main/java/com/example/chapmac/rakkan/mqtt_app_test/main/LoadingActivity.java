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

        // AppConnectionPreferences for keep latest connection before app closing.
        // Latest Connection will store in _PREFER
        _PREFER = new AppConnectionPreferences(this);

        // Check _PREFER contain any Connection or not.
        if(_PREFER.containsConnection()){

            // If it store a connection then use the same one.
            connectTo(_PREFER.getConnection());
        }else{

            // If it doesn't store connection then open ConnectionActivity.
            openNextActivity();
        }
    }

    // This method for connect to MQTT Broker.
    public void connectTo(final Connection connection){

        // Create MQTT Client.
        MqttHelper.CLIENT = new MqttAndroidClient(this.getApplicationContext(),
                "tcp://" + connection.getHost() + ":" + connection.getPort(),
                MqttClient.generateClientId());

        // MQTT Options for set the properties of MQTT Client.
        MqttHelper.OPTIONS = new MqttConnectOptions();

        // Set setAutomaticReconnect for reconnect when lost connecting from broker.
        MqttHelper.OPTIONS.setAutomaticReconnect(true);

        // Set username and password.
        MqttHelper.OPTIONS.setUserName(connection.getUser());
        MqttHelper.OPTIONS.setPassword(connection.getPass().toCharArray());

        // Start try to connect.
        try {

            // Set call back.
            IMqttToken token = MqttHelper.CLIENT.connect(MqttHelper.OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    openNextActivity();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    // This fail in case of when user start app with stored connection but no internet or the
                    // broker is closed. So delete stored connection from _PREFER and open ConnectionActivity.
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
