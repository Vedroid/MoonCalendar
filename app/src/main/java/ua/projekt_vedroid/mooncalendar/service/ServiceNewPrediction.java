package ua.projekt_vedroid.mooncalendar.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ua.projekt_vedroid.mooncalendar.R;

public class ServiceNewPrediction extends Service {

    private NotificationManager nm;
    private final int NOTIFICATION_ID = 127;
    //private static final String APP_PREFERENCES_DAYID = "DayID";

    //private Calendar calendar = Calendar.getInstance();
    //private int day = calendar.get(Calendar.DAY_OF_MONTH);
    //private int dayID;

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        //if (dayID != day) {
        //    showNotification();
        //}

    }

    public void showNotification() {
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(),
                ua.projekt_vedroid.mooncalendar.MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(),
                        0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        builder
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.moon_notific)
                .setLargeIcon(BitmapFactory.decodeResource(
                        getApplication().getResources(), R.mipmap.moon_notific))
                .setTicker("New DAY")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("PUSH UP")
                .setContentText("NEW NOTIF");
        Notification notification = builder.build();

        nm.notify(NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

