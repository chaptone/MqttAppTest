package com.example.chapmac.rakkan.mqtt_app_test;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.MqttException;

public class TemperatureActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private String topic;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private TextView textView;

    private SensorManager sensorManager;
    private Sensor sensor;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        radioGroup = findViewById(R.id.radioGroup);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textInform);
        button = findViewById(R.id.button);

        /* An event when button was clicked */

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText().equals("Publish")) {
                    /* Receive topic name from text input */
                    topic = editText.getText().toString();

                    /* Find which radio button was checked */
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);

                    /* If the temperature was checked do something */
                    if(radioButton.getText().equals("Temperature")){

                        /* Define sensor type to temperature */
                        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

                        /* Check if this device have temperature or not */
                        if(sensor!=null){

                            /* If it have.
                             * Register sensor listener to listen very value change form sensor in 10000000 period */
                            sensorManager.registerListener(sensorListener, sensor, 10000000);
                        }else{

                            /* If it doesn't have.
                             * Publish test data instead. */
                            postTestData.run();

                            textView.setHint("Your device doesn't have "+radioButton.getText()+" sensor.\nPublish test data.");
                            Toast.makeText(TemperatureActivity.this, "Temperature sensor is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(radioButton.getText().equals("Light")){
                        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                        if(sensor!=null){
                            sensorManager.registerListener(sensorListener, sensor, 10000000);
                        }else{
                            postTestData.run();
                            textView.setHint("Your device doesn't have "+radioButton.getText()+" sensor.\nPublish test data.");
                            Toast.makeText(TemperatureActivity.this, "Light sensor is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(radioButton.getText().equals("Pressure")) {
                        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
                        if (sensor != null) {
                            sensorManager.registerListener(sensorListener, sensor, 10000000);
                        } else {
                            postTestData.run();
                            textView.setHint("Your device doesn't have "+radioButton.getText()+" sensor.\nPublish test data.");
                            Toast.makeText(TemperatureActivity.this, "Pressure sensor is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(radioButton.getText().equals("Humidity")) {
                        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
                        if (sensor != null) {
                            sensorManager.registerListener(sensorListener, sensor, 10000000);
                        } else {
                            postTestData.run();
                            textView.setHint("Your device doesn't have "+radioButton.getText()+" sensor.\nPublish test data.");
                            Toast.makeText(TemperatureActivity.this, "Humidity sensor is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                    button.setText("Stop");
                } else {
                    sensorManager.unregisterListener(sensorListener);
                    button.setText("Publish");
                    textView.setHint("");
                    handler.removeCallbacks(postTestData);
                }
            }
        });
    }

    /* This method for Test data */
    private Runnable postTestData = new Runnable() {
        @Override
        public void run() {
            publish(topic,(float) Math.random()*100);
            handler.postDelayed(this,5000);
        }
    };

    /* This method for publish something with topic and value */
    public void publish(String topic,float value){
        String tempStr = value+"";
        try {
            MqttHelper.CLIENT.publish(topic, tempStr.getBytes(),0,false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /* This listener for listen value from sensor */
    SensorEventListener sensorListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        /* Get the value from sensor by event.values[0] in float */
        public void onSensorChanged(SensorEvent event) {
            publish(topic,event.values[0]);
        }
    };
}
