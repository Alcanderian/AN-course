package top.alau.gitfetch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by alcanderian on 2017/12/21.
 */

public abstract class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {
    public ArrayList<HashMap<String, Object>> data;
    public Context context;
    public Integer layoutId;
    public OnItemClickListener onItemClickListener;

    public CommonAdapter(
            Context context,
            Integer layoutId,
            ArrayList<HashMap<String, Object>> data) {
        this.data = data;
        this.context = context;
        this.layoutId = layoutId;
    }

    public abstract void convertData(ViewHolder holder, HashMap<String, Object> map);

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(layoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        convertData(holder, data.get(position));
        if (onItemClickListener != null) {
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, holder.getAdapterPosition());
                }
            });
            holder.view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemClickListener.onItemLongClick(v, holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> pool;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            pool = new SparseArray<>();
        }

        public <T extends View> T getView(int resourceId) {
            View view = pool.get(resourceId);
            if (view == null) {
                view = this.view.findViewById(resourceId);
                pool.put(resourceId, view);
            }
            return (T) view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }
}
