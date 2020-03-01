package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class WhatsappSettings extends Activity {

    private TextView phoneTextView, messageTextView;
    private Spinner prefixSpinner;
    private GlobalVariables globalVariables;
    private String[] prefixes = {"34", "93", "355", "213", "1684", "376", "244", "1264", "1268", "54", "374", "297", "247", "61", "672", "43", "994", "1242", "973", "880", "1246", "375", "32", "501", "229", "1441", "975", "591", "387", "267", "55", "1284", "673", "359", "226", "95", "257", "855", "237", "1", "238", "1345", "236", "235", "56", "86", "57", "269", "243", "242", "682", "506", "225", "385", "53", "357", "420", "45", "246", "253", "1767", "1809", "593", "20", "503", "240", "291", "372", "251", "500", "298", "679", "358", "33", "262", "594"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.whatsapp_settings);

        prefixSpinner = findViewById(R.id.whatsapp_prefix);
        phoneTextView = findViewById(R.id.whatsapp_phone);
        messageTextView = findViewById(R.id.whatsapp_message);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, prefixes);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prefixSpinner.setAdapter(aa);
    }

    public void onDone(View view) {
        String prefix = prefixSpinner.getSelectedItem().toString();
        String phone = phoneTextView.getText().toString();
        String message = messageTextView.getText().toString();

        if (prefix.length() == 0 || phone.length() == 0 || message.length() == 0) {
            Toast.makeText(this, "Parameters haven't been configured properly!", Toast.LENGTH_SHORT).show();
            return;
        }

        globalVariables = GlobalVariables.getInstance();
        globalVariables.setWhatsapp_prefix(prefix);
        globalVariables.setWhatsapp_phone(phone);
        globalVariables.setWhatsapp_message(message);
        globalVariables.setWhatsapp_set();

        Intent intent = new Intent(WhatsappSettings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
