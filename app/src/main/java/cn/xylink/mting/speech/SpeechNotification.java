package cn.xylink.mting.speech;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import cn.xylink.mting.MainActivity;
import cn.xylink.mting.R;
import cn.xylink.mting.bean.Article;
import cn.xylink.mting.speech.list.DynamicSpeechList;
import cn.xylink.mting.speech.list.SpeechList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SpeechNotification {

    SpeechService service;
    static int executeCode = 0;

    public SpeechNotification(SpeechService service) {
        this.service = service;
    }


    public void update() {
        if (service == null || service.getSelected() == null) {
            return;
        }

        Article currentArticle = service.getSelected();
        if (currentArticle == null) {
            return;
        }

        boolean hasNext = service.hasNext();
        Intent intentNotifOpen = new Intent(service, MainActivity.class);
        intentNotifOpen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 1, intentNotifOpen, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(service)
                .setContentIntent(pendingIntent)
                //.setDeleteIntent(pendingIntentCancel)
                .setSmallIcon(R.mipmap.icon_notif)
                .setLargeIcon(BitmapFactory.decodeResource(service.getResources(), R.mipmap.icon_notify_logo))
                .setTicker(currentArticle.getTitle())
                .setContentTitle("轩辕听")
                .setContentText(currentArticle.getTitle())
                .setOngoing(true)
                .setAutoCancel(false)
                .setShowWhen(false);

        //>= android 8.0 设定foregroundService的前提是notification要创建channel，并关掉channel的sound
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String channelId = "cn.xylink.mting";
            String channelName = "轩辕听";
            NotificationChannel notificationChannel = null;
            notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setSound(null, null);
            NotificationManager manager = (NotificationManager) service.getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
            //设定builder的channelid
            builder.setChannelId(channelId);
        }

        Intent playIntent = new Intent("play");
        Intent resumeIntent = new Intent("resume");
        Intent pauseIntent = new Intent("pause");
        Intent favIntent = new Intent("favorite");
        Intent unFavIntent = new Intent("unfavorite");
        Intent nextIntent = new Intent("next");
        Intent noneIntent = new Intent("null");
        Intent exitIntent = new Intent("exit");

        Notification.Action actionPlay = null, actionNext = null, actionFav = null, actionExit = null;

        boolean favorited = currentArticle.getStore() == 1;

        switch (service.getState()) {
            case Loadding:
                SpeechList speechList = service.getSpeechList();
                boolean isFirst = (speechList instanceof DynamicSpeechList && speechList.getFirst() != null && speechList.getFirst() == currentArticle);
                boolean isLast = (speechList instanceof DynamicSpeechList && speechList.getLast() != null && speechList.getLast() == currentArticle);
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, favorited ? unFavIntent : favIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_pause, "", PendingIntent.getBroadcast(service, ++executeCode, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext && !isLast ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
            case Playing:
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, favorited ? unFavIntent : favIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_pause, "", PendingIntent.getBroadcast(service, ++executeCode, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
                break;

            case Paused:
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, favorited ? unFavIntent : favIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_playing, "", PendingIntent.getBroadcast(service, ++executeCode, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
                break;

            case Error:
                actionFav = new Notification.Action(favorited ? R.mipmap.icon_favorited : R.mipmap.icon_unfavorited, "", PendingIntent.getBroadcast(service, ++executeCode, noneIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionPlay = new Notification.Action(R.mipmap.icon_playing, "", PendingIntent.getBroadcast(service, ++executeCode, playIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                actionNext = new Notification.Action(R.mipmap.next, "", PendingIntent.getBroadcast(service, ++executeCode, (hasNext ? nextIntent : noneIntent), PendingIntent.FLAG_UPDATE_CURRENT));
                break;

            default:
                return;
        }

        actionExit = new Notification.Action(R.mipmap.icon_dialog_close, "", PendingIntent.getBroadcast(service, ++executeCode, exitIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        if (actionFav != null) {
            builder.addAction(actionFav);
        }

        if (actionPlay != null) {
            builder.addAction(actionPlay);
        }

        if (actionNext != null) {
            builder.addAction(actionNext);
        }

        builder.addAction(actionExit);

        Notification.MediaStyle mediaStyle = new Notification.MediaStyle();
        mediaStyle.setShowActionsInCompactView(1, 2, 3);
        builder.setStyle(mediaStyle);

        Notification notification = builder.build();
        service.startForeground(android.os.Process.myPid(), notification);
    }

    public void stopAndNotRemove() {
        if (service != null) {
            service.stopForeground(false);
            ((NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
        }
    }


    public void stopAndRemove() {
        if (service != null) {
            service.stopForeground(true);
        }
    }
}
