package com.example.myapplication;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class KeepInAppService extends Service {

    //private static boolean sendHandler = false;
    KeepInAppHandler taskHandler = new KeepInAppHandler(this);

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Gesture listener is up!", Toast.LENGTH_LONG).show();

        //sendHandler = true;

        taskHandler.sendEmptyMessage(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //sendHandler = false;
        taskHandler.removeCallbacksAndMessages(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private class KeepInAppHandler extends Handler {
        private final WeakReference<KeepInAppService> mService;
        private Context context;
        private boolean sendHandler = false;

        public KeepInAppHandler(KeepInAppService mService) {
            this.mService = new WeakReference<KeepInAppService>(mService);
            this.context = mService;
            this.sendHandler = true;

        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ActivityManager activityManager = (ActivityManager)context.getSystemService(Service.ACTIVITY_SERVICE);

            if (activityManager.getRecentTasks(1, 0).size() > 0) {
                if (activityManager.getRecentTasks(1, 0).get(0).id != GlobalVars.taskID) {
                    // Toast.makeText(context, "Moving task to front", Toast.LENGTH_LONG).show();
                    activityManager.moveTaskToFront(GlobalVars.taskID, 0);
                }

                if (sendHandler) {
                    sendEmptyMessageDelayed(0, 1000);
                }
            }
        }

        public void stop() {
            stopSelf();
        }

    }


    public static class GlobalVars {
        public static int taskID;
        public static Intent keepInApp;
    }

}

