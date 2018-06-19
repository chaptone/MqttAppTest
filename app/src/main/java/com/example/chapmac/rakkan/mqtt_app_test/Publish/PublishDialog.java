package com.example.chapmac.rakkan.mqtt_app_test.Publish;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chapmac.rakkan.mqtt_app_test.MainActivity;
import com.example.chapmac.rakkan.mqtt_app_test.R;

import org.eclipse.paho.client.mqttv3.MqttException;

public class PublishDialog extends AppCompatDialogFragment {

    private DialogListener dialogListener;

    EditText editText1;
    EditText editText2;

    String topic;
    String message;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_publish_dialog,null);

        builder.setView(view)
                .setTitle("Add Publisher")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        publish();
                    }
                });

        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);


        return builder.create();
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
        void applyTextsFromPublishDialog(String topic,String message);
    }

    public void publish() {
        topic = editText1.getText().toString();
        message = editText2.getText().toString();

        if(topic.equals("")||message.equals("")){
            Toast.makeText(getActivity(),"Input topic or payload for publish",Toast.LENGTH_LONG).show();
        }
        else {
            try {
                MainActivity.CLIENT.publish(topic, message.getBytes(),0,false);
                dialogListener.applyTextsFromPublishDialog(topic,message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
