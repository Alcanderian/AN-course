package top.alau.alcanderian.shoplist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import java.util.Map;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MyShopApp my_app = (MyShopApp) context.getApplicationContext();
        int iid = intent.getIntExtra("itemid", 0);
        int idx = my_app.good_data.getIndex(iid);
        int imgid = my_app.good_data.imgid.get(idx);
        Map<String, Object> good_data = my_app.good_data.data.get(idx);
        Intent it_noti;
        String msg, title;
        int small_icon;

        if (intent.getAction().equals(context.getString(R.string.static_broadcast_action))) {
            it_noti = new Intent(context, DetailActivity.class);
            it_noti.putExtra("itemid", iid);
            msg = good_data.get("name").toString() + "仅售"
                    + good_data.get("price").toString();
            title = "新商品热卖";
            small_icon = R.mipmap.full_star;
        } else {
            it_noti = new Intent(context, MainActivity.class);
            it_noti.putExtra("mode", 2);
            msg = good_data.get("name").toString() + "已添加到购物车";
            title = "马上下单";
            small_icon = R.mipmap.empty_star;
        }

        PendingIntent pit_noti = PendingIntent.getActivity(context, 0, it_noti,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder bu_noti = new Notification.Builder(context)
                .setContentIntent(pit_noti)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), imgid))
                .setSmallIcon(small_icon)
                .setContentTitle(title)
                .setContentText(msg)
                .setTicker("您有一条新消息")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setOngoing(true);
        Notification noti = bu_noti.build();
        noti.flags |= Notification.FLAG_NO_CLEAR;
        NotificationManager man_noti =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        man_noti.notify(my_app.notify_id++, noti);

    }
}
