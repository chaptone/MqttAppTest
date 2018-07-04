package com.example.chapmac.rakkan.mqtt_app_test.Main;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.chapmac.rakkan.mqtt_app_test.Menu.BottomMenu;
import com.example.chapmac.rakkan.mqtt_app_test.R;

public class ConnectionCreator extends AppCompatActivity {

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutHost;
    private TextInputLayout textInputLayoutPort;
    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPass;

    private String name;
    private String host;
    private String port;
    private String user;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getTheme().applyStyle(R.style.AppTheme_Green,true);
        setContentView(R.layout.activity_connection_creator);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textInputLayoutName = findViewById(R.id.textInputLayout1);
        textInputLayoutHost = findViewById(R.id.textInputLayout2);
        textInputLayoutPort = findViewById(R.id.textInputLayout3);
        textInputLayoutUser = findViewById(R.id.textInputLayout4);
        textInputLayoutPass = findViewById(R.id.textInputLayout5);
        TextInputEditText textInputEditTextPass =findViewById(R.id.editText5);
        textInputEditTextPass.setOnEditorActionListener(editorActionListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu) {
            sendAnswerBack();
        } else{
            addDummyConnection();
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendAnswerBack(){
        if(!validateName() | !validateHost() | !validatePort()){
            return;
        }

        user = textInputLayoutUser.getEditText().getText().toString().trim();
        pass = textInputLayoutPass.getEditText().getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra("name",name);
        intent.putExtra("host",host);
        intent.putExtra("port",port);
        intent.putExtra("user",user);
        intent.putExtra("pass",pass);

        setResult(RESULT_OK,intent);
        finish();
    }

    public boolean validateName() {
        name = textInputLayoutName.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            textInputLayoutName.setError("Please assign client name");
            return false;
        } else {
            textInputLayoutName.setError(null);
            return true;
        }
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

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            switch (i) {
                case EditorInfo.IME_ACTION_SEND:
                    sendAnswerBack();
                    break;
            }
            return false;
        }
    };

    private void addDummyConnection() {
        TextInputEditText eHost =findViewById(R.id.editText2);
        TextInputEditText ePort =findViewById(R.id.editText3);
        TextInputEditText eUser =findViewById(R.id.editText4);
        TextInputEditText ePass =findViewById(R.id.editText5);

        eHost.setText("m14.cloudmqtt.com");
        ePort.setText(13988+"");
        eUser.setText("yirdixso");
        ePass.setText("1oP6Zo5aFRZe");
    }
}
