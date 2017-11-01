package top.alau.alcanderian.shoplist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Map;

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
            lv_vh.name = ret.findViewById(R.id.item_name);
            lv_vh.first = ret.findViewById(R.id.item_icon);
            lv_vh.price = ret.findViewById(R.id.item_price);
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
}