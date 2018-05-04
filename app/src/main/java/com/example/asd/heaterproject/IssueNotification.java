package com.example.asd.heaterproject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class IssueNotification {

    private Context cntx;
    private Class classActivity;

    public IssueNotification(Context mContext, Class classAct){
        this.cntx = mContext;
        this.classActivity = classAct;
    }

    public void notify(String title, String message, int notif_id){

        NotificationManager manager =
                (NotificationManager) this.cntx.getSystemService(this.cntx.NOTIFICATION_SERVICE);
        Intent goToApp =
                new Intent(cntx.getApplicationContext(), Indoors.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(cntx.getApplicationContext(),
                        0, goToApp, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this.cntx.getApplicationContext(),
                        "Thingsee App Notification Channel");
        Uri notificationSound =
                RingtoneManager.getActualDefaultRingtoneUri(this.cntx.getApplicationContext(),
                        RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(notificationSound);
        /*
        Hellooooooo ~~~~~~~~ Ngoc Son Bui ~~~~~~~~~~
        ~~~~~~~~~~~put our logo here please ~~~~~~~~
        ~~~~~~~ in the mipmap folder please ~~~~~~~~
        ~~~~~~~~ and set it to "Small Icon" ~~~~~~~~
        BR, Sontra
         */
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        manager.notify(notif_id, notification);
    }
}
