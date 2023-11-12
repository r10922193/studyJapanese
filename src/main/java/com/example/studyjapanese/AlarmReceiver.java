package com.example.studyjapanese;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    private NotificationManager notificationMgr = null;
    private static final int NOTIFICATION_FLAG = 3;

    @Override
    public void onReceive(Context context, Intent intent) {
        notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //當使用者按下通知欄中的通知時要開啟的 Activity
        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.putExtra("notiId", 1);
        //建立待處理意圖
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // 設定狀態列中的小圖片，尺寸一般建議在24×24
                .setTicker("notification on status bar.") // 設定顯示的提示文字
                .setContentTitle("Study Japanese Quiz") // 設定顯示的標題
                .setContentText("Time for you to take a quiz.") // 訊息的詳細內容
                .setContentIntent(pendingIntent) // 關聯PendingIntent
                .setNumber(1) // 在TextView的右方顯示的數字，可以在外部定義一個變數，點選累加setNumber(count),這時顯示的和
                .getNotification(); // 需要注意build()是在API level16及之後增加的，在API11中可以使用getNotificatin()來
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_FLAG, notify);

    }
}
