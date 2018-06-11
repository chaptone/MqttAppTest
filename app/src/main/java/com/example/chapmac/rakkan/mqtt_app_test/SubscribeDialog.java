package com.example.chapmac.rakkan.mqtt_app_test;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SubscribeDialog extends AppCompatDialogFragment {

    private EditText subText;
    private String subTopic;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.sub_dialog,null);

        builder.setView(view)
                .setTitle("Add subscription")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        subscribe(subText);
                    }
                });

        subText = view.findViewById(R.id.editText1);

        return builder.create();
    }

    public void subscribe(EditText subText) {
        subTopic = subText.getText().toString();
        if(subTopic.equals("")){
            Toast.makeText(getActivity(),"Input topic for subscribe"+subTopic,Toast.LENGTH_LONG).show();
        }else {
            int qos = 1;
            try {
                IMqttToken subToken = MainActivity.CLIENT.subscribe(subTopic, qos);
                subToken.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {

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
