package com.example.chapmac.rakkan.mqtt_app_test;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private ProgressBar proBar;

    public static MqttAndroidClient CLIENT;
    MqttConnectOptions OPTIONS;

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
        OPTIONS.setAutomaticReconnect(true);
        OPTIONS.setUserName(user);
        OPTIONS.setPassword(pass.toCharArray());
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
