package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private GlobalVariables globalVariables;
    private Spinner communicationSpinner;
    private String[] communicationMethods = {"Telegram", "Whatsapp"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_settings);

        communicationSpinner = findViewById(R.id.communication_spinner);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, communicationMethods);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        communicationSpinner.setAdapter(aa);
    }

    public void goToTelegramSettings(View view) {
        Intent intent = new Intent(MainActivity.this, TelegramSettings.class);
        startActivity(intent);
    }

    public void goToWhatsappSettings(View view) {
        Intent intent = new Intent(MainActivity.this, WhatsappSettings.class);
        startActivity(intent);
    }

    public void launchGestureListenerActivity(View view) {

        globalVariables = GlobalVariables.getInstance();
        String communicationMethod = communicationSpinner.getSelectedItem().toString();

        if (globalVariables.isConfigured()) {
            globalVariables.setCommunicationMethod(communicationMethod);
            Intent intent = new Intent(MainActivity.this, GestureListenerActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "You have to configure a communication method first!", Toast.LENGTH_LONG).show();
        }
    }
}
