package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

    private ProgressBar proBar;

    public static MqttAndroidClient CLIENT;
    MqttConnectOptions OPTIONS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        proBar = findViewById(R.id.progressBar);
        proBar.setVisibility(View.GONE);

        textInputLayoutHost = findViewById(R.id.textInputLayout1);
        textInputLayoutPort = findViewById(R.id.textInputLayout2);
        textInputLayoutUser = findViewById(R.id.textInputLayout3);
        textInputLayoutPass = findViewById(R.id.textInputLayout4);

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

    public void connect(View v) {
        if(!validateHost() | !validatePort()){
            return;
        }
        proBar.setVisibility(View.VISIBLE);

        user = textInputLayoutUser.getEditText().getText().toString();
        pass = textInputLayoutPass.getEditText().getText().toString();

        host_root = "tcp://" + host + ":" + port;
        String clientId = MqttClient.generateClientId();
        CLIENT = new MqttAndroidClient(this.getApplicationContext(), host_root, clientId);

        OPTIONS = new MqttConnectOptions();
        OPTIONS.setUserName(user);
        OPTIONS.setPassword(pass.toCharArray());
        try {
            IMqttToken token = CLIENT.connect(OPTIONS);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(MainActivity.this, "Connected!!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, TabActivity.class);
                    startActivity(intent);
                    proBar.setVisibility(View.GONE);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    proBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Connected Failed", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
