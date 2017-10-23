package top.alau.alcanderian.shoplist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected RecyclerView rv_items;
    protected ListView lv_items;
    protected ShopApp my_app;
    protected FloatingActionButton fab_tochart;
    protected int mode; // 1.item;  2.chart.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_app = (ShopApp) getApplication();
        rv_items = (RecyclerView) findViewById(R.id.itemlist);
        lv_items = (ListView) findViewById(R.id.chartlist);
        fab_tochart = (FloatingActionButton) findViewById(R.id.to_chart);
        mode = 1;

        lv_items.setVisibility(View.GONE);

        fab_tochart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode == 1) mode = 2;
                else mode = 1;
                if(mode == 1){
                    lv_items.setVisibility(View.GONE);
                    rv_items.setVisibility(View.VISIBLE);
                    fab_tochart.setImageResource(R.mipmap.shoplist);
                } else {
                    lv_items.setVisibility(View.VISIBLE);
                    rv_items.setVisibility(View.GONE);
                    fab_tochart.setImageResource(R.mipmap.mainpage);
                }
            }
        });

        // Item Part.
        MyRcAdpater rcad = new MyRcAdpater(MainActivity.this, R.layout.view_item,
                new ArrayList<>(my_app.item_data.data), new ArrayList<>(my_app.item_data.itemid)) {
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

        rcad.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onClick(MyRcAdpater rcad, int position) {
                Intent it_item = new Intent();
                it_item.setClass(MainActivity.this, DetailActivity.class);
                it_item.putExtra("itemid", rcad.ad_dataid.get(position));
                startActivity(it_item);
            }

            @Override
            public void onLongClick(MyRcAdpater rcad, int position) {
                rcad.ad_dataid.remove(position);
                rcad.ad_data.remove(position);
                rcad.notifyItemRemoved(position);
                rcad.notifyItemRangeChanged(position, rcad.ad_data.size());
                Toast.makeText(rcad.ad_ctx,
                        "移除第 " + position + "个商品", Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayoutManager ly_man = new LinearLayoutManager(MainActivity.this);
        rv_items.setLayoutManager(ly_man);
        ly_man.setOrientation(OrientationHelper.VERTICAL);
        rv_items.setAdapter(rcad);
        rv_items.setItemAnimator(new DefaultItemAnimator());

        // Chart Part.
        final MyLvAdapter lvad = new MyLvAdapter(MainActivity.this, my_app.chart_data.data,
                my_app.chart_data.itemid);

        lv_items.setAdapter(lvad);
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    Intent it_item = new Intent();
                    it_item.setClass(MainActivity.this, DetailActivity.class);
                    it_item.putExtra("itemid", my_app.chart_data.itemid.get(position));
                    startActivity(it_item);
                }
            }
        });
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                if(position != 0) {
                    AlertDialog.Builder ad_builder;
                    ad_builder = new AlertDialog.Builder(MainActivity.this);
                    ad_builder
                            .setTitle("移除商品")
                            .setMessage("从购物车移除"
                                    + my_app.chart_data.data.get(position).get("name").toString()
                                    + "？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    my_app.chart_data.data.remove(position);
                                    my_app.chart_data.itemid.remove(position);
                                    my_app.chart_data.stared.remove(position);
                                    lvad.notifyDataSetChanged();
                                }
                            }).create().show();
                }
                return true;
            }
        });
    }

    public interface MyOnItemClickListener {
        void onClick(MyRcAdpater rcad, int position);

        void onLongClick(MyRcAdpater rcad, int position);
    }

    public class MyRcAdpater extends RecyclerView.Adapter<MyRcAdpater.MyRcViewHolder> {

        ArrayList<Map<String, Object>> ad_data;
        ArrayList<Integer> ad_dataid;
        Context ad_ctx;
        int ad_lid;
        MyOnItemClickListener ad_click;

        public MyRcAdpater(Context ctx, int lay_id, ArrayList<Map<String, Object>> data,
                           ArrayList<Integer> data_id) {
            ad_ctx = ctx;
            ad_lid = lay_id;
            ad_data = data;
            ad_dataid = data_id;
        }

        public void convert(MyRcViewHolder vh, Map<String, Object> m) {
        }

        public void setOnItemClickListener(MyOnItemClickListener onItemClickListener) {
            ad_click = onItemClickListener;
        }

        @Override
        public void onBindViewHolder(final MyRcViewHolder vh, int pos) {
            convert(vh, ad_data.get(pos));
            if (ad_click != null) {
                vh.v_me.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad_click.onClick(MyRcAdpater.this, vh.getAdapterPosition());
                    }
                });
                vh.v_me.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ad_click.onLongClick(MyRcAdpater.this, vh.getAdapterPosition());
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return ad_data.size();
        }

        @Override
        public MyRcViewHolder onCreateViewHolder(ViewGroup vg, int v_type) {
            return makeMyRcViewHolder(ad_ctx, vg, ad_lid);
        }

        public class MyRcViewHolder extends RecyclerView.ViewHolder {
            private SparseArray<View> v_sub;
            private View v_me;

            public MyRcViewHolder(View v_item) {
                super(v_item);
                v_me = v_item;
                v_sub = new SparseArray<>();
            }

            public <T extends View> T getView(int v_id) {
                View v = v_sub.get(v_id);
                if (v == null) {
                    v = v_me.findViewById(v_id);
                    v_sub.put(v_id, v);
                }
                return (T) v;
            }
        }

        public MyRcViewHolder makeMyRcViewHolder(Context ctx, ViewGroup parent, int lay_id) {
            return new MyRcViewHolder(LayoutInflater.from(ctx).inflate(lay_id, parent, false));
        }
    }

    public class MyLvAdapter extends BaseAdapter {
        Context lv_ctx;
        ArrayList<Map<String, Object>> lv_data;
        ArrayList<Integer> lv_dataid;

        public MyLvAdapter(Context ctx, ArrayList<Map<String, Object>> data,
                           ArrayList<Integer> data_id) {
            lv_ctx = ctx;
            lv_data = data;
            lv_dataid = data_id;
        }

        @Override
        public int getCount() {
            return lv_data.size();
        }

        @Override
        public Map<String, Object> getItem(int i) {
            return lv_data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup vg) {
            View ret;
            MyLvViewHolder lv_vh;
            if (v == null) {
                ret = LayoutInflater.from(lv_ctx).inflate(R.layout.view_item, null);
                lv_vh = new MyLvViewHolder();
                lv_vh.name = (TextView) ret.findViewById(R.id.item_name);
                lv_vh.first = (TextView) ret.findViewById(R.id.item_icon);
                lv_vh.price = (TextView) ret.findViewById(R.id.item_price);
                ret.setTag(lv_vh);
            } else {
                ret = v;
                lv_vh = (MyLvViewHolder) ret.getTag();
            }
            lv_vh.name.setText(lv_data.get(i).get("name").toString());
            lv_vh.first.setText(lv_data.get(i).get("first").toString());
            lv_vh.price.setText(lv_data.get(i).get("price").toString());

            return ret;
        }

        public class MyLvViewHolder {
            public TextView first;
            public TextView name;
            public TextView price;
        }
    }
}
