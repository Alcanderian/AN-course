package top.alau.alcanderian.shoplist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.Map;

public class MyRcAdpater extends RecyclerView.Adapter<MyRcViewHolder> {

    ArrayList<Map<String, Object>> ad_data;
    ArrayList<Integer> ad_dataid;
    Context ad_ctx;
    int ad_lid;
    boolean scroll_direction;
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
                    ad_click.onClick(vh.getAdapterPosition());
                }
            });
            vh.v_me.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ad_click.onLongClick(vh.getAdapterPosition());
                    return true;
                }
            });
        }
        if (scroll_direction) {
            vh.itemView.setAnimation(AnimationUtils.loadAnimation(ad_ctx, R.anim.rotate_in_top));
        } else {
            vh.itemView.setAnimation(AnimationUtils.loadAnimation(ad_ctx, R.anim.rotate_in_down));
        }
    }

    @Override
    public void onViewDetachedFromWindow(MyRcViewHolder vh) {
        super.onViewDetachedFromWindow(vh);
        vh.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return ad_data.size();
    }

    @Override
    public MyRcViewHolder onCreateViewHolder(ViewGroup vg, int v_type) {
        return makeMyRcViewHolder(ad_ctx, vg, ad_lid);
    }

    public MyRcViewHolder makeMyRcViewHolder(Context ctx, ViewGroup parent, int lay_id) {
        return new MyRcViewHolder(LayoutInflater.from(ctx).inflate(lay_id, parent, false));
    }
}