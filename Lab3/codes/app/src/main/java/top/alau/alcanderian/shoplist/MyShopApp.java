package top.alau.alcanderian.shoplist;

import android.app.Application;
import android.widget.ListView;

public class MyShopApp extends Application {
    public ItemData good_data;
    public ItemData cart_data;
    public MyLvAdapter lvad;
    @Override
    public void onCreate() {
        super.onCreate();
        good_data = new ItemData(true);
        cart_data = new ItemData(false);
    }
}
