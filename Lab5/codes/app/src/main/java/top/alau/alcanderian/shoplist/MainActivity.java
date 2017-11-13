package top.alau.alcanderian.shoplist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected RecyclerView rv_items;
    protected ListView lv_items;
    protected MyShopApp my_app;
    protected FloatingActionButton fab_mode;
    protected int view_mode; // 1.item;  2.cart.

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyEvent e) {
        if (e.msg.equals("refresh cart")) {
            ((MyLvAdapter) lv_items.getAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_app = (MyShopApp) getApplication();
        rv_items = (RecyclerView) findViewById(R.id.itemlist);
        lv_items = (ListView) findViewById(R.id.cartlist);
        fab_mode = (FloatingActionButton) findViewById(R.id.mode_switch);
        view_mode = 1;

        onNewIntent(getIntent());

        update_mode();

        EventBus.getDefault().register(this);

        sendBroadcast(new Intent()
                .setAction(getResources().getString(R.string.static_broadcast_action))
                .putExtra("itemid", new Random().nextInt(my_app.good_data.size()))
        );

        fab_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_mode == 1) view_mode = 2;
                else view_mode = 1;
                update_mode();
            }
        });

        // Item Part.
        MyRcAdpater rc_ad = new MyRcAdpater(MainActivity.this, R.layout.view_item,
                new ArrayList<>(my_app.good_data.data), new ArrayList<>(my_app.good_data.itemid)) {
            @Override
            public void convert(MyRcViewHolder vh, Map<String, Object> m) {
                TextView icon = vh.getView(R.id.item_icon);
                TextView name = vh.getView(R.id.item_name);
                TextView price = vh.getView(R.id.item_price);
                name.setText(m.get("name").toString());
                icon.setText(m.get("first").toString());
                price.setText("");
            }
        };

        rc_ad.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onClick(int position) {
                MyRcAdpater rc_ad = (MyRcAdpater) rv_items.getAdapter();
                Intent it_item = new Intent();
                it_item.setClass(MainActivity.this, DetailActivity.class);
                it_item.putExtra("itemid", rc_ad.ad_dataid.get(position));
                startActivity(it_item);
            }

            @Override
            public void onLongClick(int position) {
                MyRcAdpater rc_ad = (MyRcAdpater) rv_items.getAdapter();
                rc_ad.ad_dataid.remove(position);
                rc_ad.ad_data.remove(position);
                rc_ad.notifyItemRemoved(position);
                rc_ad.notifyItemRangeChanged(position, rc_ad.ad_data.size());
                Toast.makeText(rc_ad.ad_ctx,
                        "移除第 " + position + "个商品", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager ly_man = new LinearLayoutManager(MainActivity.this);
        rv_items.setLayoutManager(ly_man);
        ly_man.setOrientation(OrientationHelper.VERTICAL);
        rv_items.setAdapter(rc_ad);
        rv_items.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                MyRcAdpater rc_ad = (MyRcAdpater) rv_items.getAdapter();
                rc_ad.scroll_direction = dy > 0;
            }
        });

        // Cart Part.
        MyLvAdapter lvad = new MyLvAdapter(MainActivity.this, my_app.cart_data.data,
                my_app.cart_data.itemid);

        lv_items.setAdapter(lvad);
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    Intent it_item = new Intent();
                    it_item.setClass(MainActivity.this, DetailActivity.class);
                    it_item.putExtra("itemid", my_app.cart_data.itemid.get(position));
                    startActivity(it_item);
                }
            }
        });
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                if (position != 0) {
                    AlertDialog.Builder ad_builder;
                    ad_builder = new AlertDialog.Builder(MainActivity.this);
                    ad_builder
                            .setTitle("移除商品")
                            .setMessage("从购物车移除"
                                    + my_app.cart_data.data.get(position).get("name").toString()
                                    + "？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    my_app.cart_data.remove(position);
                                    ((MyLvAdapter) lv_items.getAdapter()).notifyDataSetChanged();
                                }
                            }).create().show();
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onNewIntent(Intent it) {
        super.onNewIntent(it);
        if(it.hasExtra("mode")) {
            view_mode = it.getIntExtra("mode", 1);
            update_mode();
        }
    }

    public void update_mode() {
        if (view_mode == 1) {
            lv_items.setVisibility(View.GONE);
            rv_items.setVisibility(View.VISIBLE);
            fab_mode.setImageResource(R.mipmap.shoplist);
        } else {
            lv_items.setVisibility(View.VISIBLE);
            rv_items.setVisibility(View.GONE);
            fab_mode.setImageResource(R.mipmap.mainpage);
        }
    }
}
