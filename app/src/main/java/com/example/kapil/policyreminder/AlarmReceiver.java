package com.example.kapil.policyreminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.example.kapil.policyreminder.AddNewActivity.TAG;
import static java.net.Proxy.Type.HTTP;

/**
 * Created by KAPIL on 12-01-2018.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        Notification myNotication;
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);


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
        int ID = intent.getIntExtra("ID",0);
        Toast.makeText(context, "SMS sent to "+name+" !", Toast.LENGTH_LONG).show();
        // builder.setAutoCancel(false);
        builder.setTicker("this is ticker text");
        builder.setContentTitle("Reminder");
        builder.setContentText(name + "'s policy is about to expire soon!");
        builder.setSmallIcon(R.drawable.reminder_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.reminder_icon));
        //builder.setContentIntent(pendingIntent);
        builder.setNumber(100);
        builder.build();
        myNotication = builder.getNotification();
        manager.notify(ID, myNotication);

        String policyNum = intent.getStringExtra("POLICYNUM");
        String receiver = intent.getStringExtra("NUMBER");
        try {
            SmsManager.getDefault().sendTextMessage(receiver,null,"Your Policy with policy number "+policyNum+" is due for renewal!\n(Ignore if renewed)\nRegards:\nKapil Gupta",null,null);
        }catch(Exception e){
            Log.d(TAG, "onReceive: " + e);
        }
//        Intent sendIntent = new Intent(android.content.Intent.ACTION_SENDTO);
//        sendIntent.setData(Uri.parse("mailto:"));
//        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]
//                {receiver});
//        sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Policy renewal reminder!");
//        sendIntent.putExtra(android.content.Intent.EXTRA_TEXT,"Your Policy is due for renewal! \n Ignore if renewed. \n Regards: \n Kapil Gupta\n 9999663750");
//
//        //sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
//        sendIntent.setType("text/html");
//        context.startActivity(sendIntent);
//        Log.d(TAG, "onReceive: " + receiver);
    }
}
