package com.example.chapmac.rakkan.mqtt_app_test.Publish;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapmac.rakkan.mqtt_app_test.MainActivity;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class PublishFragment extends Fragment {

    TextView textView;

    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        textView = view.findViewById(R.id.textView);

        return view;
    }

    public void addPublisher(String topic,String message){
        textView.setText(topic+message);
    }
}
