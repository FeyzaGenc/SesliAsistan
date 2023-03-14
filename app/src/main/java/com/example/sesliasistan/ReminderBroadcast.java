package com.example.sesliasistan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    public static int notificationId = 200;

    public static String messageExtra = "messageExtra";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("myTag","on reminder broadcast received");
        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"bildiriSesliAsistan")
                .setSmallIcon(R.drawable.ic_mic)
                .setContentTitle("Sesli Asistan hatirlatma")
                .setContentText(intent.getStringExtra(messageExtra))
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        NotificationManagerCompat notificationManager=NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId,builder.build());
//        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        if (alarmUri == null)
//        {
//            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        }
//        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
//        ringtone.play();

    }
}
