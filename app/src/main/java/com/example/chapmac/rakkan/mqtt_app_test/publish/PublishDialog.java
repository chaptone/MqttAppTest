package com.example.chapmac.rakkan.mqtt_app_test.publish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.chapmac.rakkan.mqtt_app_test.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class PublishDialog extends AppCompatDialogFragment {

    private DialogListener dialogListener;

    private TextInputLayout textInputTopic;
    private TextInputLayout textInputMessage;

    private String topic;
    private String message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_publish_dialog,null);

        builder.setView(view)
                .setTitle("Add Publisher")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Add",null);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!validateTopic() | !validateMessage()) {
                        }else{
                            publish();
                            dialog.dismiss();
                        }
                    }
                });

                Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negativeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        textInputTopic = view.findViewById(R.id.textInputLayout1);
        textInputMessage = view.findViewById(R.id.textInputLayout2);

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dialogListener = (DialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+" must implement DialogListener");
        }
    }

    public interface DialogListener{
        void applyTextsFromPublishDialog(String status , String topic , String message);
    }

    public boolean validateTopic() {
        topic = textInputTopic.getEditText().getText().toString().trim();

        if (topic.isEmpty()) {
            textInputTopic.setError("Can't publish empty topic");
            return false;
        } else {
            textInputTopic.setError(null);
            return true;
        }
    }

    public boolean validateMessage() {
        message = textInputMessage.getEditText().getText().toString().trim();

        if (message.isEmpty()) {
            textInputMessage.setError("Can't publish topic with empty message");
            return false;
        } else {
            textInputMessage.setError(null);
            return true;
        }
    }

    public void publish() {
        try {
            MqttHelper.CLIENT.publish(topic, message.getBytes(),0,false)
                    .setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    dialogListener.applyTextsFromPublishDialog("successful",topic,message);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    dialogListener.applyTextsFromPublishDialog("Failed",topic,message);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
