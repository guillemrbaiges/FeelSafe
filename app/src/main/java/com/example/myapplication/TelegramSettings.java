package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class TelegramSettings extends Activity {

    private TextView chatIdTextView, messageTextView;
    private GlobalVariables globalVariables;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.telegram_settings);

        chatIdTextView = findViewById(R.id.telegram_chatid);
        messageTextView = findViewById(R.id.telegram_message);
    }

    public void onDone(View view) {
        String chatId = chatIdTextView.getText().toString();
        String message = messageTextView.getText().toString();

        if (chatId.length() == 0 || message.length() == 0) {
            Toast.makeText(this, "Parameters haven't been configured properly!", Toast.LENGTH_SHORT).show();
            return;
        }

        globalVariables = GlobalVariables.getInstance();
        globalVariables.setTelegram_chatId(chatId);
        globalVariables.setTelegram_message(message);
        globalVariables.setTelegram_set();

        Intent intent = new Intent(TelegramSettings.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
