package top.alau.alcanderian.shoplist;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

public class MyRcViewHolder extends RecyclerView.ViewHolder {
    public SparseArray<View> v_basket;
    public View v_me;

    public MyRcViewHolder(View v_item) {
        super(v_item);
        v_me = v_item;
        v_basket = new SparseArray<>();
    }

    public <T extends View> T getView(int v_id) {
        View v = v_basket.get(v_id);
        if (v == null) {
            v = v_me.findViewById(v_id);
            v_basket.put(v_id, v);
        }
        return (T) v;
    }
}