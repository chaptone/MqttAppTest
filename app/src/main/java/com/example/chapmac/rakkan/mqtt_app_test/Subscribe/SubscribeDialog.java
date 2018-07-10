package com.example.chapmac.rakkan.mqtt_app_test.Subscribe;

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
import android.widget.Toast;

import com.example.chapmac.rakkan.mqtt_app_test.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SubscribeDialog extends AppCompatDialogFragment {

    private TextInputLayout textInputTopic;
    private String subTopic;
    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_subscribe_dialog, null);

        builder.setView(view)
                .setTitle("Add subscription")
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
                        if (!validateTopic()) {
                        }else{
                            subscribe();
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

        textInputTopic = view.findViewById(R.id.textInputLayout);

        return alertDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    public interface DialogListener {
        void applyTextsFromSubscribeDialog(String status , String topic);
    }

    public boolean validateTopic() {
        subTopic = textInputTopic.getEditText().getText().toString().trim();

        if (subTopic.isEmpty()) {
            textInputTopic.setError("Can't subscribe empty topic");
            return false;
        } else {
            textInputTopic.setError(null);
            return true;
        }
    }

    public void subscribe() {
        int qos = 1;
        try {
            IMqttToken subToken = MqttHelper.CLIENT.subscribe(subTopic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    dialogListener.applyTextsFromSubscribeDialog("successful",subTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    dialogListener.applyTextsFromSubscribeDialog("Failed",subTopic);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
