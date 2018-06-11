package com.example.chapmac.rakkan.mqtt_app_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;


/**
 * A simple {@link Fragment} subclass.
 */
public class SubscribeFragment extends Fragment {

    private String subTopic;

    public SubscribeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);

        Button subBtn = view.findViewById(R.id.button2);
        subBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subscribe();
            }
        });

        return view;
    }

    public void subscribe() {
        EditText editText3 = getActivity().findViewById(R.id.editText3);
        subTopic = editText3.getText().toString();
        if(subTopic.equals("")){
            Toast.makeText(getActivity(),"Input topic for subscribe"+subTopic,Toast.LENGTH_LONG).show();
        }else {
            int qos = 1;
            try {
                IMqttToken subToken = MainActivity.CLIENT.subscribe(subTopic, qos);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Toast.makeText(getActivity(),"Subscribe to "+subTopic,Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken,
                                          Throwable exception) {
                        // The subscription could not be performed, maybe the user was not
                        // authorized to subscribe on the specified topic e.g. using wildcards
                        Toast.makeText(getActivity(),"Failed to subscribe "+subTopic,Toast.LENGTH_LONG).show();
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

}
