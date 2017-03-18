package ua.projekt_vedroid.mooncalendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Projekt on 18.03.2017.
 */

public class NotificReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, ServiceNewPrediction.class));
    }
}
