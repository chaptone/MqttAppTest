package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_HOST = "com.example.chapmac.rakkan.mqtt_app_test.EXTRA_HOST";
    public static final String EXTRA_USERNAME = "com.example.chapmac.rakkan.mqtt_app_test.EXTRA_USERNAME";
    public static final String EXTRA_PASSWORD = "com.example.chapmac.rakkan.mqtt_app_test.EXTRA_PASSWORD";

    private String host ;
    private String user ;
    private String pass ;

    MqttAndroidClient client;

    MqttConnectOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



    }

    public void connect(View v){
        EditText editText1 = findViewById(R.id.editText1);
        host = editText1.getText().toString();

        EditText editText2 = findViewById(R.id.editText2);
        user = editText2.getText().toString();

        EditText editText3 = findViewById(R.id.editText3);
        pass = editText3.getText().toString();

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), host, clientId);

        options = new MqttConnectOptions();
        options.setUserName(user);
        options.setPassword(pass.toCharArray());
        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this,"Connected!!",Toast.LENGTH_LONG).show();
                    openActivity2();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(MainActivity.this,"Connected Failed",Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void openActivity2() {
        Intent intent = new Intent(this,Activity2.class);
        intent.putExtra(EXTRA_HOST,host);
        intent.putExtra(EXTRA_USERNAME,user);
        intent.putExtra(EXTRA_PASSWORD,pass);
        startActivity(intent);
    }

}
