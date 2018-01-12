package com.example.kapil.policyreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.kapil.policyreminder.AddNewActivity.TAG;

/**
 * Created by KAPIL on 12-01-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification myNotication;
        Log.d(TAG, "onReceive: " + intent);
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Toast.makeText(context, "Alarm! Wake up! Wake up!", Toast.LENGTH_LONG).show();
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null)
        {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();

        //Intent intent1 = new Intent("com.rj.notitfications.SECACTIVITY");
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, 0);
        //Log.d(TAG, "onReceive: " + pendingIntent);

        Notification.Builder builder = new Notification.Builder(context);

        String  name = intent.getStringExtra("Name");
        Log.d(TAG, "onReceive: " + name);

        // builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Reminder");
        builder.setContentText(name);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        //builder.setContentIntent(pendingIntent);
        builder.setNumber(100);
        builder.build();
        //Log.d(TAG, "onReceive: " + "WORK");

        myNotication = builder.getNotification();
        manager.notify(11, myNotication);

    }
}
