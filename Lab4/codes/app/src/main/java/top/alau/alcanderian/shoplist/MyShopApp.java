package top.alau.alcanderian.shoplist;

import android.app.Application;
import android.content.Intent;
import android.widget.ListView;

import java.util.Random;

public class MyShopApp extends Application {
    public ItemData good_data;
    public ItemData cart_data;
    public int notify_id = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        good_data = new ItemData(true);
        cart_data = new ItemData(false);
    }
}
