package top.alau.alcanderian.shoplist;

import android.app.Application;

public class ShopApp extends Application {
    public ItemData item_data;
    public ItemData chart_data;

    @Override
    public void onCreate() {
        super.onCreate();
        item_data = new ItemData(true);
        chart_data = new ItemData(false);
    }
}
