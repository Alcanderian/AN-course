package top.alau.alcanderian.shoplist;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.Map;

public class ShopperWidget extends AppWidgetProvider {

    static MyShopApp my_app;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        my_app = (MyShopApp) context.getApplicationContext();
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shopper_widget);
        views.setImageViewResource(R.id.wimg, R.mipmap.ic_launcher_round);
        views.setTextViewText(R.id.wtxt, "当前没有任何消息");
        Intent it = new Intent(context, MainActivity.class);
        PendingIntent pit = PendingIntent
                .getActivity(context, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.wshoplay, pit);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(context.getString(R.string.static_broadcast_action)) ||
                intent.getAction().equals(context.getString(R.string.dynamic_broadcast_action))) {
            my_app = (MyShopApp) context.getApplicationContext();
            int iid = intent.getIntExtra("itemid", 0);
            int idx = my_app.good_data.getIndex(iid);
            int imgid = my_app.good_data.imgid.get(idx);
            Map<String, Object> good_data = my_app.good_data.data.get(idx);
            Intent it_noti;
            String msg;

            if (intent.getAction().equals(context.getString(R.string.static_broadcast_action))) {
                it_noti = new Intent(context, DetailActivity.class);
                it_noti.putExtra("itemid", iid);
                msg = good_data.get("name").toString() + "仅售"
                        + good_data.get("price").toString();
            } else {
                it_noti = new Intent(context, MainActivity.class);
                it_noti.putExtra("mode", 2);
                msg = good_data.get("name").toString() + "已添加到购物车";
            }

            PendingIntent pit = PendingIntent.getActivity(context, 0, it_noti,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shopper_widget);
            views.setImageViewResource(R.id.wimg, imgid);
            views.setTextViewText(R.id.wtxt, msg);
            views.setOnClickPendingIntent(R.id.wshoplay, pit);

            ComponentName me = new ComponentName(context, ShopperWidget.class);
            AppWidgetManager awm = AppWidgetManager.getInstance(context);

            awm.updateAppWidget(me, views);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

