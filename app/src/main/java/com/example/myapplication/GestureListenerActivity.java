package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ajts.androidmads.telegrambotlibrary.Telegram;
import com.ajts.androidmads.telegrambotlibrary.Utils.TelegramCallback;
import com.ajts.androidmads.telegrambotlibrary.models.GetMe;
import com.ajts.androidmads.telegrambotlibrary.models.Message;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

import okhttp3.Call;

// TO RUN EMULATOR RUN FIRST: sudo chown guillemrbaiges /dev/kvm

public class GestureListenerActivity extends AppCompatActivity implements GestureOverlayView.OnGesturePerformedListener {

    private WhatsappAccessibilityService accessibility;
    private GestureLibrary gLibrary;
    private FusedLocationProviderClient fusedLocationClient;
    private Telegram telegramBot;
    private GlobalVariables globalVariables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        /* Create gestures library*/
        gLibrary =
                GestureLibraries.fromRawResource(this, R.raw.gesture);
        if (!gLibrary.load()) {
            finish();
        }

        GestureOverlayView gOverlay =
                (GestureOverlayView) findViewById(R.id.gOverlay);
        gOverlay.addOnGesturePerformedListener(this);

        /* Define location client */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        /* Define accessiblity auxiliar class */
        accessibility = new WhatsappAccessibilityService();

        /* Initialise & Authorize telegram bot */
        telegramBot = new Telegram("1107747571:AAEx1c8gw3AzYm0VK_pV70UbSBUX3oulc1c");
        telegramBot.getMe(new TelegramCallback<GetMe>() {
            @Override
            public void onResponse(Call call, final GetMe getMe) {
                Log.v("response.body()", getMe.isOk() + "");
            }

            @Override
            public void onFailure(Call call, Exception e) {

            }
        });


        KeepInAppService.GlobalVars.taskID = getTaskId();
        KeepInAppService.GlobalVars.keepInApp = new Intent(this, KeepInAppService.class);
        startService(KeepInAppService.GlobalVars.keepInApp);
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {

        final ArrayList<Prediction> predictions =
                gLibrary.recognize(gesture);

        if (predictions.size() > 0 && predictions.get(0).score > 1.0) {

            String action = predictions.get(0).name;

            if (action.equals("Triangle")) {
                // Toast.makeText(GestureListenerActivity.this, action, Toast.LENGTH_SHORT).show();

                /* Getting last location */
                fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                globalVariables = GlobalVariables.getInstance();
                                String communicationMethod = globalVariables.getCommunicationMethod();

                                if (communicationMethod == "Telegram") {
                                    sendTelegramAlert(location);
                                } else if (communicationMethod == "Whatsapp") {
                                    sendWhatsappAlert(location);
                                }
                            }
                        }
                    });
            }
        }
    }

    public void sendWhatsappAlert(Location location) {
        globalVariables = GlobalVariables.getInstance();
        String prefix = globalVariables .getWhatsapp_prefix();
        String phone = globalVariables.getWhatsapp_phone();
        String message = globalVariables.getWhatsapp_message();
        String locationMessage = mapsUrlFromLocation(location);

        PackageManager packageManager = getPackageManager();    // Check why this not used
        Intent i = new Intent(Intent.ACTION_VIEW);              // Check why this not used

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setPackage("com.whatsapp");
        intent.putExtra("jid", prefix + phone + "@s.whatsapp.net");

        if (intent != null) {
            intent.putExtra(Intent.EXTRA_TEXT, locationMessage + "\n\n" + message);//
            startActivity(Intent.createChooser(intent, message));
        } else {
            Toast.makeText(this, "Whatsapp not found", Toast.LENGTH_SHORT).show();
        }

        if (!accessibility.isAccessibilityOn (GestureListenerActivity.this, WhatsappAccessibilityService.class)) {
            Intent accessibilityIntent = new Intent (Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity (accessibilityIntent);
        }

        try {
            Thread.sleep (1000); // hack for certain devices in which the immediate back click is too fast to handle
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Toast.makeText(GestureListenerActivity.this, "Messaged successfully sent through whatsapp!", Toast.LENGTH_LONG).show();
    }

    public void sendTelegramAlert(Location location) {
        globalVariables = GlobalVariables.getInstance();
        String chatId = globalVariables.getTelegram_chatId();
        String message = globalVariables.getTelegram_message();
        String locationMessage = mapsUrlFromLocation(location);

        telegramBot.sendMessage(chatId, message, new TelegramCallback<Message>() {
            @Override
            public void onResponse(Call call, Message response) {
                Log.v("response.body()", response.isOk() + "");
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Log.d("TELEGRAM ", "Failed on sendMessage");
            }
        });

        telegramBot.sendMessage(chatId, locationMessage, new TelegramCallback<Message>() {
            @Override
            public void onResponse(Call call, Message response) {
                Log.v("response.body()", response.isOk() + "");
            }

            @Override
            public void onFailure(Call call, Exception e) {
                Log.d("TELEGRAM ", "Failed on sendMessage");
            }
        });

        Toast.makeText(GestureListenerActivity.this, "Message successfully sent through telegram!", Toast.LENGTH_LONG).show();
    }

    private String mapsUrlFromLocation(final Location location) {
        return "http://www.google.com/maps/place/" + Location.convert(location.getLatitude(), Location.FORMAT_DEGREES) + "," + Location.convert(location.getLongitude(), Location.FORMAT_DEGREES);
    }
}
