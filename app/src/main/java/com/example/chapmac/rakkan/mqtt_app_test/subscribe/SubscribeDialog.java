package com.example.chapmac.rakkan.mqtt_app_test.subscribe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.chapmac.rakkan.mqtt_app_test.utility.MqttHelper;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

public class SubscribeDialog extends AppCompatDialogFragment {

    private TextInputLayout textInputTopic;
    private String subTopic;
    private DialogListener dialogListener;
    private List<String> topicList, existTopicList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_subscribe_dialog, null);

        if (getArguments() != null) {
            topicList = getArguments().getStringArrayList("topic_list");
            existTopicList = getArguments().getStringArrayList("exist_topic_list");
        }

        AutoCompleteTextView editText = view.findViewById(R.id.actv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, topicList.toArray(new String[topicList.size()]));
        editText.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Add subscription")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Add",null);

        final AlertDialog alertDialog = builder.create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {

                // Ok button
                Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Check text input is empty or not.
                        if (!validateTopic()) {
                        }else{

                            // Try to subscribe.
                            subscribe();
                            dialog.dismiss();
                        }
                    }
                });

                // Cancel button.
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

    // Start subscribe to MQTT broker.
    public void subscribe() {
        int qos = 1;
        try {
            IMqttToken subToken = MqttHelper.CLIENT.subscribe(subTopic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    // If success sent acknowledge back to TabActivity.
                    dialogListener.applyTextsFromSubscribeDialog("successful",subTopic);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {

                    // If fail sent acknowledge back to TabActivity.
                    dialogListener.applyTextsFromSubscribeDialog("Failed",subTopic);
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
