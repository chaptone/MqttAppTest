package com.example.chapmac.rakkan.mqtt_app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


/**
 * A simple {@link Fragment} subclass.
 */
public class PublishFragment extends Fragment {

    private Button pubBtn;

    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        pubBtn = view.findViewById(R.id.button);
        pubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText1 = getActivity().findViewById(R.id.editText1);
                String topic = editText1.getText().toString();

                EditText editText2 = getActivity().findViewById(R.id.editText2);
                String message = editText2.getText().toString();

                if(topic.equals("")||message.equals("")){
                    Toast.makeText(getActivity(),"Input topic or payload for publish",Toast.LENGTH_LONG).show();
                }
                else {
                    try {
                        MainActivity.CLIENT.publish(topic, message.getBytes(),0,false);
                        Toast.makeText(getActivity(),"Publish "+ topic,Toast.LENGTH_LONG).show();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return view;
    }
}
