package ua.projekt_vedroid.mooncalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ServiceNewPrediction extends Service {

    private NotificationManager nm;
    private static int lunarDay;
    private static int day = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        URLCon urlCon = new URLCon(MainActivity.URL);
        urlCon.start();                                         //Запуск потока
        waitThread(urlCon);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    public int onStartCommand(Intent intent, int flags, int startID) {
        if (lunarDay != day) {
            showNotification();
            day = lunarDay;
        }
        return super.onStartCommand(intent, flags, startID);
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
                .setTicker("New DAY")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("PUSH UP")
                .setContentText("NEW NOTIF");
        Notification notification = builder.build();

        nm.notify(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void waitThread(Thread thread) {
        if (thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setLunarDay(String inputFirstLine) {
        lunarDay = Integer.parseInt(inputFirstLine);
    }
}