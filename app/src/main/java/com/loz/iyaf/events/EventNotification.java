package com.loz.iyaf.events;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.loz.R;
import com.loz.iyaf.feed.EventData;
import com.loz.iyaf.mainmenu.MenuActivity;

import java.text.SimpleDateFormat;


public class EventNotification {
    private static final String NOTIFICATION_TITLE = "Event starting soon";
    private static final String NOTIFICATION_TEXT_FORMAT = "%s will be starting at %s";

    private static final Long MILLIS_BEFORE_EVENT_NOTIFICATION = 3600000L;

    public static void setNotification(EventData event, Activity activity) {
        createChannel(activity);
        scheduleNotification(getNotification(activity, event), activity, event);

    }

    public static void removeNotification(EventData event, Activity activity) {
        Log.d("LOZ", "Removing notification "+event.getName()+" "+event.getNotificaitonId());
        PendingIntent pendingIntent = getIntent(activity, getNotification(activity, event), event);
        AlarmManager manager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }


    private static void scheduleNotification(Notification notification, Activity activity, EventData event) {

        PendingIntent pendingIntent = getIntent(activity, notification, event);

        long futureInMillis = event.getStartTime().getTime() - MILLIS_BEFORE_EVENT_NOTIFICATION;
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
        }
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Favourite: "+event.getName())
                .putContentType("Favourite")
                .putContentId("f"+event.getId()));
    }

    private static PendingIntent getIntent(Activity activity, Notification notification, EventData event) {
        Intent notificationIntent = new Intent(activity, MenuActivity.class);
        notificationIntent.setClass(activity, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, event.getNotificaitonId());
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, event.getNotificaitonId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }

    private static Notification getNotification(Activity activity, EventData event) {
        String channelId = activity.getString(R.string.favourites_pref_key);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String startTime = localDateFormat.format(event.getStartTime());
        String notificationText = String.format(NOTIFICATION_TEXT_FORMAT, event.getName(), startTime);
        Uri notificationSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(activity, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, channelId)
                .setSmallIcon(R.drawable.ic_event_black_24dp)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent)
                .setSound(notificationSoundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        return mBuilder.build();
    }

    private static void createChannel(Activity activity) {
        // Create notification channel if not already present
        String channelId = activity.getString(R.string.favourites_pref_key);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            NotificationManager notificationManager = (NotificationManager) activity.getSystemService(
                    activity.NOTIFICATION_SERVICE);
            if (notificationManager.getNotificationChannel(channelId) == null) {
                CharSequence name = activity.getString(R.string.channel_name);
                String description = activity.getString(R.string.channel_description);
                NotificationChannel channel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(description);
                // Register the channel with the system
                if (notificationManager.getNotificationChannel(channelId) == null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }
        }
        Log.d("LOZ", "channelId " + channelId);
    }


}
