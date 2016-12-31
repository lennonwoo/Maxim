package com.lennonwoo.maxim.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.lennonwoo.maxim.MaximPreference;
import com.lennonwoo.maxim.R;
import com.lennonwoo.maxim.db.MaximDB;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MaximService extends Service {

    public static String TAG = MaximService.class.getSimpleName();

    private Notification noti;
    private NotificationManager notiManager;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleBroadcast(intent);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Bind");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(br, filter);
        notiManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        noti = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_bookmark_border_white_24dp)
                .setAutoCancel(true)
                .setOngoing(false)
                .build();
        noti.contentView = new RemoteViews(getPackageName(), R.layout.noti_content_normal);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private void handleBroadcast(Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_ON:
                SharedPreferences pref = getSharedPreferences(MaximPreference.PREF_NAME, Context.MODE_PRIVATE);
                SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                try {
                    Date startTime = parser.parse(pref.getString(MaximPreference.START_TIME, "00:00"));
                    Date endTime = parser.parse(pref.getString(MaximPreference.END_TIME, "24:00"));
                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int minute = Calendar.getInstance().get(Calendar.MINUTE);
                    Date currentTime = parser.parse(hour + ":" + minute);
                    boolean toggleRandom = pref.getBoolean(MaximPreference.MAXIM_TOGGLE_RANDOM, false);
                    if (toggleNoti(startTime, endTime, currentTime, toggleRandom))
                        showMaximNoti();
                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Parse time failed", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private boolean toggleNoti(Date startTime, Date endTime, Date currentTime, boolean toggleRandom) {
        boolean date = currentTime.before(endTime) && currentTime.after(startTime);
        boolean random = System.currentTimeMillis() % 2 == 0;
        if (toggleRandom) return date && random;
        else return date;
    }

    private void showMaximNoti() {
        MaximDB maximDB = MaximDB.getInstance(this);
        String s = maximDB.getRandomMaxim();
        if (s != null) {
            noti.contentView.setTextViewText(R.id.noti_maxim, s);
            notiManager.notify(s.hashCode(), noti);
        }
    }
}
