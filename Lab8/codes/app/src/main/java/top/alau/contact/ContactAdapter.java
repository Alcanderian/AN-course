package top.alau.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alcanderian on 2017/12/19.
 */

public class ContactAdapter extends BaseAdapter {
    public ArrayList<ContactItem> data;
    private Context context;
    private OnItemClickListener onItemClickListener = null;

    public ContactAdapter(Context context, ArrayList<ContactItem> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View ret;
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            ret = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            vh.name = ret.findViewById(R.id.txt_name);
            vh.birth = ret.findViewById(R.id.txt_birth);
            vh.gift = ret.findViewById(R.id.txt_gift);
            ret.setTag(vh);
        } else {
            ret = convertView;
            vh = (ViewHolder) ret.getTag();
        }

        if(onItemClickListener != null) {
            ret.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(ret, position);
                }
            });

            ret.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(ret, position);
                    return true;
                }
            });
        }
        vh.name.setText(data.get(position).name);
        vh.birth.setText(data.get(position).birth);
        vh.gift.setText(data.get(position).gift);
        return ret;
    }

    @Override
    public ContactItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private static class ViewHolder {
        TextView name;
        TextView birth;
        TextView gift;
    }

    interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
